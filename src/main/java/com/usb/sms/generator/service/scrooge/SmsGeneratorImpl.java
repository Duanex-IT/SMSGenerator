package com.usb.sms.generator.service.scrooge;

import com.usb.esb.proxies.scrooge.model.ScroogeSmsEntity;
import com.usb.esb.proxies.scrooge.model.ScroogeSmsWrapper;
import com.usb.esb.proxies.smsgate.model.InputSmsMessage;
import com.usb.esb.proxies.smsgate.model.SendResultList;
import com.usb.esb.proxies.smsgate.model.SmsMessageType;
import com.usb.sms.generator.utils.RestClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SmsGeneratorImpl implements SmsGenerator {

    @Autowired
    RestClientUtil rest;

    @Async
    @Scheduled(cron = "${schedule.cron.sendSmsFromScrooge}")
//    @Scheduled(initialDelay = 60*1000, fixedRate=300*1000)
    public void sendSmsFromScrooge() {
        System.out.println("sendSmsFromScrooge");
        //call esb scrooge
        ScroogeSmsWrapper smsData = rest.httpGet("scrooge/rest/sms/getSms", new HashMap<>(), ScroogeSmsWrapper.class);
        System.out.println(smsData);

        //prepare sms
        List<InputSmsMessage> messages = new ArrayList<>();
        for (ScroogeSmsEntity entity : smsData.getSmss()) {
            InputSmsMessage message = new InputSmsMessage();
            message.setPhone(entity.getPhone());
            message.setSmsText(entity.getSmsText());
            message.setType(SmsMessageType.DEFAULT);//todo
            messages.add(message);
        }

        //call sms
        if (messages.size()>0) {
            SendResultList results = rest.httpPost("sms/rest/SmsSender/sendSmsMessages", messages, SendResultList.class);
        }
    }

}
