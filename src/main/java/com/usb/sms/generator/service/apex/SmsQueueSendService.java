package com.usb.sms.generator.service.apex;

import com.usb.esb.proxies.smsgate.model.InputSmsMessage;
import com.usb.esb.proxies.smsgate.model.SendResultList;
import com.usb.esb.proxies.smsgate.model.SmsMessageType;
import com.usb.sms.generator.service.apex.model.Config;
import com.usb.sms.generator.utils.RestClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SmsQueueSendService {
    private static final Logger logger = LoggerFactory.getLogger(SmsQueueSendService.class);
    private static final int MAX_SEND_QUEUE_SIZE = 50;
    @Autowired
    private RestClientUtil restClientUtil;

    @Autowired
    private ApexSmsDbService dbService;
    private final List<InputSmsMessage> queue = Collections.synchronizedList(new ArrayList<InputSmsMessage>(MAX_SEND_QUEUE_SIZE));
    private final List<Long> sentMessages = Collections.synchronizedList(new ArrayList<Long>(MAX_SEND_QUEUE_SIZE));

    private Config config;

    final RowCallbackHandler smsRawCallbackHandler = new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            long id = rs.getLong("id");
            String phone = rs.getString("phone");
            String sms= rs.getString("text");
            if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(sms)) {
                InputSmsMessage smsMessage = new InputSmsMessage();
                smsMessage.setSmsText(sms);
                smsMessage.setPhone(phone);
                smsMessage.setType(SmsMessageType.DEFAULT);//todo
                queue.add(smsMessage);
                sentMessages.add(id);
            }
            if (queue.size()>= MAX_SEND_QUEUE_SIZE){
                sendMessages();
            }
        }
    };

    private void sendMessages(){
        if (queue.size()==0){
            return;
        }
        try {
            SendResultList results = restClientUtil.httpPost("sms/rest/SmsSender/sendSmsMessages", queue, SendResultList.class);
            if (config.isKeepSmsPool()) {
                dbService.setSmsMessageSent(sentMessages);
            } else {
                dbService.removeSmsMessagesFromPool(sentMessages);
            }
        } catch (RestClientException | DataAccessException e){
            logger.error("Can't post smsMessages", e);
        }
        queue.clear();
        sentMessages.clear();
    }

    @Async("sendSmsThreadExecutor")
    public void sendSmsFromQueue(){
        try{
            queue.clear();
            sentMessages.clear();
            config = dbService.getConfig();
            dbService.getSmsMessagesFromQueue(smsRawCallbackHandler);
            sendMessages();
        } catch (DataAccessException e){
            logger.error("Can't post sms messages from queue");
        }
    }
}
