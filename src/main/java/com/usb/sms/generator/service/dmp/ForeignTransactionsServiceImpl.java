package com.usb.sms.generator.service.dmp;

import com.usb.sms.generator.mapper.DmpTransactionsMapper;
import com.usb.sms.generator.utils.RestClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ForeignTransactionsServiceImpl implements ForeignTransactionsService {

    @Autowired
    RestClientUtil rest;

    @Autowired
    DmpTransactionsMapper transactionsMapper;

    @Async
//    @Scheduled(cron = "${schedule.cron.sendSmsFromScrooge}")
//    @Scheduled(initialDelay = 60*1000, fixedRate=300*1000)
    public void process() {
        //get transactions
        transactionsMapper.getTransactions();

        //filter transactions

        //scrooge user

        //create sms objects, send sms

        //upsert to siebel

    }

}
