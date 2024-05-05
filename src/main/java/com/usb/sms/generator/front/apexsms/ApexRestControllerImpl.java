package com.usb.sms.generator.front.apexsms;

import com.usb.sms.generator.service.apex.ApexSmsDbService;
import com.usb.sms.generator.service.apex.model.Config;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@Api(value = "Apex SMS generator control api", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
@RequestMapping(value = "/apexsms")
public class ApexRestControllerImpl implements ApexRestController{
    final static private Logger logger = LoggerFactory.getLogger(ApexSmsDbService.class);
    @Autowired
    ApexSmsDbService dbService;

    @RequestMapping(value = "/enableApexService", method = RequestMethod.POST)
    @ApiOperation(value = "Enable Apex SMS service", httpMethod = "POST", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    @Override
    public ResponseEntity<String> enableApexService() {
        logger.info("Enabling APEX SMS Generator service");
        try {
            dbService.setApexSmsGeneratorEnabledState(true);
        } catch (DataAccessException e){
            logger.error("Can't enable APEX SMS Generator service", e);
            return new ResponseEntity<String>("Can't enable APEX SMS Generator service. \nError: " + e.getLocalizedMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<String>("Apex SMS Generator service enabled", HttpStatus.OK);
    }

    @RequestMapping(value = "/disableApexService", method = RequestMethod.POST)
    @ApiOperation(value = "Disable Apex SMS service", httpMethod = "POST", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    @Override
    public ResponseEntity<String> disableApexService() {
        logger.info("Disabling APEX SMS Generator service");
        try {
            dbService.setApexSmsGeneratorEnabledState(false);
        } catch (DataAccessException e){
            logger.error("Can't disable APEX SMS Generator service", e);
            return new ResponseEntity<String>("Can't disable APEX SMS Generator service. \nError: " + e.getLocalizedMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<String>("Apex SMS Generator service disabled!", HttpStatus.OK);
    }

    @RequestMapping(value = "/keepSmsInApexSmsPool", method = RequestMethod.POST)
    @ApiOperation(value = "Keep Messages in apex SMS Pool", httpMethod = "POST", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    @Override
    public ResponseEntity<String> keepSmsInApexSmsPool(@ApiParam(value = "Keep or not messages in pool", required = true, allowableValues = "true,false") @RequestParam Boolean keep) {
        logger.info("Setting {} for keep messages in sms pool", keep);
        try {
            dbService.setKeepMessagesInPool(keep);
        } catch (DataAccessException e){
            logger.error("Can't set value for keep sms messages in pool", e);
            return new ResponseEntity<String>("Can't set value for keep sms messages in pool. \nError: " + e.getLocalizedMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<String>(keep?"Messages will be kept in pool":"Messages wil be deleted from pool", HttpStatus.OK);
    }

    @RequestMapping(value = "/clearLastTransaction", method = RequestMethod.POST)
    @ApiOperation(value = "Clears last read APEX transactionId", httpMethod = "POST", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    @Override
    public ResponseEntity<String> clearLastReadTransactionId() {
        logger.info("Clears last read APEX transactionId");
        try {
            dbService.saveLastTransactionId(0);
        } catch (DataAccessException e){
            logger.error("Can't clear last transaction id", e);
            return new ResponseEntity<String>("Can't clear last transaction id. \nError: " + e.getLocalizedMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<String>("Last transaction id cleared", HttpStatus.OK);
    }


    @RequestMapping(value = "/getRestrictedAccounts", method = RequestMethod.GET)
    @ApiOperation(value = "Returns restricted accounts list", httpMethod = "GET", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    @Override
    public ResponseEntity<String> getRestrictedAccounts() {
        Config config = dbService.getConfig();
        return new ResponseEntity<String>(StringUtils.collectionToCommaDelimitedString(config.getAccountsToInclude()), HttpStatus.OK);
    }

    @RequestMapping(value = "/setRestrictedAccounts", method = RequestMethod.POST)
    @ApiOperation(value = "Set restricted accounts list", httpMethod = "POST", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
    @Override
    public ResponseEntity<String> setRestrictedAccounts(@ApiParam(value = "Restricted accounts comma separated list. If 'none' received - clears accounts list", required = true, allowableValues = "['comma delimited list of accounts', none]")
                                                            @RequestParam(required = true) String accountsList) {
        Set<String> accounts = new HashSet<>();
        logger.debug("Received new account list: {}", accountsList);
        if ("none".equals(accountsList.trim())){
            logger.info("Clearing restricted accounts list");
        } else {
            accounts.addAll(StringUtils.commaDelimitedListToSet(accountsList));
            if (accounts.isEmpty()){
                logger.error("Invalid accounts list.");
                return new ResponseEntity<String>("Invalid accounts list.", HttpStatus.METHOD_NOT_ALLOWED);
            }
            logger.info("Setting new account list: {}", StringUtils.collectionToCommaDelimitedString(accounts));
        }
        try {
            dbService.setAccountsInclude(accounts);
        } catch (DataAccessException e){
            logger.error("Can't update restricted account list", e);
            return new ResponseEntity<String>("Can't update restricted account listю \nError: " + e.getLocalizedMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
        return new ResponseEntity<String>("New Restrincted accounts: " + StringUtils.collectionToCommaDelimitedString(accounts), HttpStatus.OK);
    }
}
