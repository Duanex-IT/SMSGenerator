package com.usb.sms.generator.service.apex;

import com.sun.istack.internal.NotNull;
import com.usb.sms.generator.service.apex.model.Config;
import com.usb.sms.generator.service.apex.model.SmsTemplateConfig;
import com.usb.sms.generator.service.apex.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class ApexSmsDbService {

    final static private Logger logger = LoggerFactory.getLogger(ApexSmsDbService.class);

    @Autowired
    private JdbcTemplate configApexSmsJdbcTemplate;

    @Autowired
    private JdbcTemplate apexJdbcTemplate;
    private static final String SQL_GET_SMS_FROM_QUEUE = "SELECT \n" +
            "PHONE, TEXT, IS_DONE, \n" +
            "   ID\n" +
            "FROM ESB.SMS_APEX_POOL where is_done=0";

    public Config getConfig() {

        try {
            Config config = configApexSmsJdbcTemplate.queryForObject("SELECT \n" +
                    "LAST_READ_TRANSACTION_ID, is_enabled, keep_sms_pool, accounts_include \n" +
                    "FROM SMS_APEX_CONFIG", new RowMapper<Config>() {
                @Override
                public Config mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Config config = new Config();
                    config.setLastTransactionId(rs.getLong("LAST_READ_TRANSACTION_ID"));
                    config.setIsEnabled(rs.getInt("is_enabled") == 1);
                    config.setKeepSmsPool(rs.getInt("keep_sms_pool") == 1);
                    String accountsToInclude = rs.getString("accounts_include");
                    if (!StringUtils.isEmpty(accountsToInclude)) {
                        Set<String> accounts = StringUtils.commaDelimitedListToSet(accountsToInclude);
                        if (!accounts.isEmpty()) {
                            config.setAccountsToInclude(accounts);
                        }
                    }
                    return config;
                }
            });
            return config;
        } catch (DataAccessException e) {
            logger.error("Can't get last transaction id", e);
        }
        return new Config();
    }

    public void setApexSmsGeneratorEnabledState(boolean enabled) throws DataAccessException{
        logger.info("Setting apex sms generator enabled state to {}", enabled);
        configApexSmsJdbcTemplate.update("update SMS_APEX_CONFIG set is_enabled = ?", enabled?1:0);
    }

    public void setKeepMessagesInPool(boolean keep) throws DataAccessException{
        logger.info("Setting keep messages in pool state to {}", keep);
        configApexSmsJdbcTemplate.update("update SMS_APEX_CONFIG set keep_sms_pool = ?", keep? 1 : 0);
    }

    public void setAccountsInclude(@NotNull Set<String> accountsList) throws DataAccessException{
        assert accountsList!=null;
        String list = StringUtils.collectionToCommaDelimitedString(accountsList);
        logger.info("Setting restricted accounts list to {}", list);
        configApexSmsJdbcTemplate.update("update SMS_APEX_CONFIG set accounts_include = ?", list);
    }

    @Transactional
    public void saveLastTransactionId(long lastTransactionId) {
        logger.debug("Saving last transaction id {}", lastTransactionId);

        try {
            boolean isConfigExists = configApexSmsJdbcTemplate.queryForObject("select count(*) from SMS_APEX_CONFIG", Integer.class) > 0;
            if (isConfigExists) {
                configApexSmsJdbcTemplate.update("update SMS_APEX_CONFIG set LAST_READ_TRANSACTION_ID = ?", lastTransactionId);
            } else {
                configApexSmsJdbcTemplate.update("insert into SMS_APEX_CONFIG(LAST_READ_TRANSACTION_ID, is_enabled) values (?, 1)", lastTransactionId);
            }
        } catch (DataAccessException e) {
            logger.error("Can't update last transaction id", e);
        }
    }

    @Transactional
    public void saveSmsQueue(List<Transaction> transactions) {
        String sql = "INSERT INTO ESB.SMS_APEX_POOL (\n" +
                "   ID, PHONE, TEXT, IS_DONE) \n" +
                "VALUES (SMS_APEX_QUEUE_SEQ.nextval, ?,\n" +
                " ?,\n" +
                " 0 )";

        configApexSmsJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Transaction tr = transactions.get(i);
                ps.setString(1, tr.getPhone());
                ps.setString(2, tr.getSmsText());
            }

            @Override
            public int getBatchSize() {
                return transactions.size();
            }
        });
    }

    public void getSmsMessagesFromQueue(@NotNull RowCallbackHandler rowCallbackHandler) {
        configApexSmsJdbcTemplate.query(SQL_GET_SMS_FROM_QUEUE, rowCallbackHandler);
    }

    public void setSmsMessageSent(List<Long> ids) {
        configApexSmsJdbcTemplate.batchUpdate("update ESB.SMS_APEX_POOL set is_done =1 where id=?", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

    public void removeSmsMessagesFromPool(List<Long> ids) {
        configApexSmsJdbcTemplate.batchUpdate("delete from ESB.SMS_APEX_POOL where id=? or is_done=1", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

    /**
     * Retreive transactions from apex
     *
     * @param lastTransactionId  min transactionId+1 to search
     * @param actionIds          collection of action ids to search
     * @param rowCallbackHandler callback handler for process resultset
     * @throws DataAccessException
     */
    public void getTransactionsFromApex(long lastTransactionId, @NotNull Set<Integer> actionIds, @NotNull RowCallbackHandler rowCallbackHandler, Set<String> accountToInclude) throws DataAccessException {
        String actionsSql = StringUtils.collectionToCommaDelimitedString(actionIds);
        String sql = "select AA.ACCACTIONARCHID, AA.ARCHREGDATE, AA.ACCACTIONID, aa.accid, AA.ACTIONAMT, AA.ACTIONCCY, AA.COMMENTS, \n" +
                "CLIENTS.PHONE, to_char(aa.operdate, 'dd.mm.yyyy') as oper_date, to_char(aa.operdate, 'mm.yyyy') as oper_month \n" +
                "from CIC.ACCACTIONARCH aa, CIC.ACCOUNTS, CIC.CLIENTS, CIC.OPERATIONARCH oa where \n" +
                "AA.ACCACTIONARCHID>? \n" +
                "and AA.OPERATIONARCHID = oa.OPERATIONARCHID " +
                "and oa.recstatusid = 12 "+
                "and ACCOUNTS.ACCID = AA.ACCID and CLIENTS.CLIENTID = ACCOUNTS.CLIENTID and \n aa.ACCACTIONID in (" + actionsSql + ")";
        if (accountToInclude != null && !accountToInclude.isEmpty()) {

            StringJoiner joiner = new StringJoiner(",");
            for (String account : accountToInclude) {
                joiner.add("'" + account.trim() + "'");
            }
            logger.debug("Get sms from apex restricted only to accounts: {}", joiner.toString());
            sql = sql + "\n and ACCOUNTS.accnum in (" + joiner.toString() + ")";
        }
        sql = sql + " order by AA.ACCACTIONARCHID";
        /* for test */
        String sql1 = "select AA.ACCACTIONARCHID, AA.ARCHREGDATE, AA.ACCACTIONID, aa.accid, AA.ACTIONAMT, AA.ACTIONCCY, AA.COMMENTS, \n" +
                "CLIENTS.PHONE, to_char(operdate, 'dd.mm.yyyy') as oper_date, to_char(aa.operdate, 'mm.yyyy') as oper_month\n" +
                "from CIC.ACCACTIONARCH aa, CIC.ACCOUNTS, CIC.CLIENTS where \n" +
                "aa.operdate > to_date(?,'dd.mm.yyyy') and aa.accid =713 \n" +
                "and ACCOUNTS.ACCID = AA.ACCID and CLIENTS.CLIENTID = ACCOUNTS.CLIENTID and aa.ACCACTIONID in (" + actionsSql + ")";

        apexJdbcTemplate.query(sql, rowCallbackHandler, lastTransactionId);
        //apexJdbcTemplate.query(sql1, rowCallbackHandler, "29.06.2015");
    }

    /**
     * Find cashback transactions
     *
     * @param originalTransaction Original percent transaction
     * @param cashBackCodes       actionIds of cashback transactions
     * @return
     */
    public List<Transaction> findCashBackTransactions(Transaction originalTransaction, @NotNull Integer[] cashBackCodes) {
        try {
            String cashBackCodesStr = StringUtils.arrayToCommaDelimitedString(cashBackCodes);
            List<Transaction> result = apexJdbcTemplate.query("select AA.ACCACTIONARCHID, AA.ARCHREGDATE, AA.ACCACTIONID, to_char(operdate, 'dd.mm.yyyy') as oper_date, " +
                            "aa.accid, AA.ACTIONAMT, AA.ACTIONCCY, AA.COMMENTS, to_char(aa.operdate, 'mm.yyyy') as oper_month\n" +
                            "from CIC.ACCACTIONARCH aa\n" +
                            "where AA.ACCID =? \n" +
                            "and trunc(aa.operdate, 'MONTH') = trunc(to_date(?,'dd.mm.yyyy'), 'MONTH')\n" +
                            "and aa.operdate <= trunc(to_date(?,'dd.mm.yyyy'))\n" +
                            "and AA.ACCACTIONID in(" + cashBackCodesStr + ") \n" +
                            "and (\n" + // filter transactions before 15 date of month and after
                            "case when operdate>trunc(aa.operdate,'month')+14 and to_date(?,'dd.mm.yyyy')>trunc(aa.operdate,'month')+14\n" +
                            "then\n" +
                            "'valid'\n" +
                            "when operdate<=trunc(aa.operdate,'month')+14 and to_date(?,'dd.mm.yyyy')<=trunc(aa.operdate,'month')+14\n" +
                            "then 'valid'\n" +
                            "else\n" +
                            "'invalid'\n" +
                            "end\n" +
                            ") ='valid' \n" +
                            "and aa.ACTIONCCY = ? \n",
                    new Object[]{originalTransaction.getAccountId(),
                            originalTransaction.getOperDate(), originalTransaction.getOperDate(), originalTransaction.getOperDate(), originalTransaction.getOperDate(),
                            originalTransaction.getCurrency()},
                    (rs, rowNum) -> {
                        return new Transaction(null, rs.getInt("ACCACTIONID"), rs.getLong("accid"), rs.getDouble("ACTIONAMT"), rs.getString("ACTIONCCY"),
                                rs.getString("OPER_DATE"), rs.getString("oper_month"));
                    }
            );
            return result;
        } catch (DataAccessException e) {
            logger.error("Can't find cashback transaction", e);
        }
        return null;
    }

    /**
     * Loads sms templates from database
     *
     * @return
     */
    public Map<Integer, SmsTemplateConfig> getTemplates() {
        Map<Integer, SmsTemplateConfig> templatesMap = new HashMap<>();
        try {
            configApexSmsJdbcTemplate.query("select * from sms_apex_templates where is_active=1", rs -> {
                SmsTemplateConfig config = new SmsTemplateConfig();
                config.setId(rs.getLong("id"));
                config.setActionId(rs.getInt("action_id"));
                config.setDescription(rs.getString("description"));
                config.setTemplate(rs.getString("template"));
                templatesMap.put(config.getActionId(), config);
            });
        } catch (DataAccessException e) {
            logger.error("Can't get templates list.", e);
        }
        return templatesMap;

    }

    public long getMaxApexTransactionId() {
        Long result = null;
        try {
            result = apexJdbcTemplate.queryForObject("SELECT max(ACCACTIONARCHID) \n" +
                    "from CIC.ACCACTIONARCH", Long.class);
        } catch (DataAccessException e) {
            logger.error("Can't get last transaction id", e);
        }
        return result == null ? 0 : result;
    }

}
