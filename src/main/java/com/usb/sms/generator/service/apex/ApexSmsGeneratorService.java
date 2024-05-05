package com.usb.sms.generator.service.apex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usb.sms.generator.service.apex.model.Config;
import com.usb.sms.generator.service.apex.model.SmsTemplateConfig;
import com.usb.sms.generator.service.apex.model.Transaction;
import com.usb.sms.generator.utils.CurrencyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class ApexSmsGeneratorService {
    final static private Logger logger = LoggerFactory.getLogger(ApexSmsGeneratorService.class);
    @Autowired
    private ApexSmsDbService apexDbService;

    private final AtomicInteger recordCountProceeded = new AtomicInteger();

    @Autowired
    SmsQueueSendService smsQueueSendService;

    private long lastTransactionId;
    /**
     * Action ids of percent transactions. Need to calculate sum without cashback
     */
    private static final Integer[] PERCENT_ACTION_IDS = new Integer[]{11311, 11111, 11091};

    private static final Integer[] CASHBACK_ACTION_IDS = new Integer[]{-965, -920, -902};

    private static final Double TAX_PERCENT = new BigDecimal("0.215").doubleValue(); // 21.5%

    private Map<Integer, SmsTemplateConfig> configList;
    private static final int QUEUE_LENGTH = 100;
    final List<Transaction> smsQueue = new ArrayList<>(QUEUE_LENGTH);

    @Async
    @Scheduled(cron = "${schedule.cron.sendSmsFromApex}")
    //@Scheduled(fixedRate = 5 * 60 * 1000)
    @Transactional(value = "apexSmsTransactionManager",
            rollbackFor = DataAccessException.class,
            readOnly = false,
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED)
    public void sendSmsFromApex() {
        logger.debug("Send SMS from APEX triggered");
        smsQueue.clear();
        recordCountProceeded.set(0);
        Config config = apexDbService.getConfig();
        logger.debug("Apex sms config {}", config);
        if (!config.isEnabled()) {
            logger.info("Apex sms is disabled. Exiting...");
            return;
        }
        if (config.getLastTransactionId() == 0) {
            logger.debug("Last transaction not saved. Getting max transaction id from apex");
            long lastTransactionId = apexDbService.getMaxApexTransactionId();
            logger.debug("Last apex transaction id {}", lastTransactionId);
            apexDbService.saveLastTransactionId(lastTransactionId);
            logger.debug("Exiting...");
            return;
        } else {
            lastTransactionId = config.getLastTransactionId();
            configList = apexDbService.getTemplates();
            logger.debug("Loaded {} templates", configList.size());
            if (configList.isEmpty()) {
                logger.error("Sms templates list is empty. Exiting...");
                return;
            }
            Set<Integer> actionIds = configList.keySet();
            final RowCallbackHandler rowCallbackHandler = rs -> {
                recordCountProceeded.incrementAndGet();
                processTransactions(rs);
                long transactionId = rs.getLong("ACCACTIONARCHID");
                if (lastTransactionId < transactionId) {
                    lastTransactionId = transactionId;
                }
            };
            logger.debug("Fetching transactions with transactionId>{}", lastTransactionId);
            try {

                apexDbService.getTransactionsFromApex(lastTransactionId, actionIds, rowCallbackHandler, config.getAccountsToInclude());
                if (!smsQueue.isEmpty()) {
                    apexDbService.saveSmsQueue(smsQueue);
                    smsQueue.clear();
                }
                apexDbService.saveLastTransactionId(lastTransactionId);
                logger.info("Generation finished. Last TransactionId [{}], Proceeded records [{}]", lastTransactionId, recordCountProceeded.longValue());
            } catch (DataAccessException e) {
                logger.error("Can't process transaction list from apex.", e);
                smsQueue.clear();
                throw e;
            }
            smsQueueSendService.sendSmsFromQueue();
        }

    }


    /**
     * Process transaction row from resultset
     *
     * @param rs
     * @throws SQLException
     */
    private void processTransactions(ResultSet rs) throws SQLException {
        String phone = rs.getString("phone");
        if (StringUtils.isEmpty(phone)) {
            //skip client with empty phone
            return;
        }
        Double amount = rs.getDouble("ACTIONAMT");
        String currency = rs.getString("ACTIONCCY");
        int actionId = rs.getInt("ACCACTIONID");
        String operDate = rs.getString("oper_date");
        long accountId = rs.getLong("accid");
        String operMonth = rs.getString("oper_month");
        Transaction transaction = new Transaction(phone, actionId, accountId, amount, currency, operDate, operMonth);
        //logger.debug("Got transaction {}", transaction);
        if (isPercentTransaction(transaction)) {
            logger.debug("Transaction {} is percent transaction. try to find cashback transaction.", transaction);
            List<Transaction> cashbackTransactions = apexDbService.findCashBackTransactions(transaction, CASHBACK_ACTION_IDS);
            if (cashbackTransactions != null && cashbackTransactions.size() > 0) {
                logger.info("Got {} cashback transactions for transaction {}", cashbackTransactions.size(), transaction);
                for (Transaction cashbackTransaction : cashbackTransactions) {
                    amount = CurrencyUtils.minus(amount, cashbackTransaction.getAmount());
                }

                transaction.setAmount(amount);
            }
            if (transaction.getAmount().doubleValue() == 0d) {
                logger.debug("Skipping transaction {}. Calculated Amount = 0", transaction);
                return;
            }
            // calculate amount without tax
            Double amountWithoutTax = CurrencyUtils.minusTax(transaction.getAmount(), TAX_PERCENT);
            transaction.setAmount(amountWithoutTax);
            logger.info("Calculated percent transaction: {}", transaction);
        } else if (isCashBackTransaction(transaction)) {
            // calculate amount without tax
            Double amountWithoutTax = CurrencyUtils.minusTax(transaction.getAmount(), TAX_PERCENT);
            transaction.setAmount(amountWithoutTax);
            logger.info("Calculated cashback transaction: {}", transaction);
        }

        if (prepareSmsTextForTransaction(transaction)) {
            smsQueue.add(transaction);
            if (smsQueue.size() >= QUEUE_LENGTH) {
                apexDbService.saveSmsQueue(smsQueue);
                smsQueue.clear();
            }
        }
    }

    private boolean prepareSmsTextForTransaction(Transaction transaction) {
        SmsTemplateConfig templateConfig = configList.get(transaction.getActionId());
        if (templateConfig != null && !StringUtils.isEmpty(transaction.getPhone())) {
            String template = templateConfig.getTemplate();
            Map<String, Object> props = getFieldsMap(transaction);
            String smsText = com.usb.sms.generator.utils.StringUtils.interpolate(template, props);
            if (smsText.length() > 255) {
                smsText = smsText.substring(0, 255);
            }
            logger.debug("Got sms text: {}", smsText);
            transaction.setSmsText(smsText);
            return true;
        }
        return false;
    }

    public Map<String, Object> getFieldsMap(Transaction transaction) {
        ObjectMapper m = new ObjectMapper();
        Map props = m.convertValue(transaction, Map.class);
        return props;
    }


    private boolean isPercentTransaction(Transaction transaction) {
        for (Integer percentActionId : PERCENT_ACTION_IDS) {
            if (percentActionId == transaction.getActionId()) {
                return true;
            }
        }
        return false;
    }

    private boolean isCashBackTransaction(Transaction transaction) {
        for (Integer actionId : CASHBACK_ACTION_IDS) {
            if (actionId == transaction.getActionId()) {
                return true;
            }
        }
        return false;
    }


}
