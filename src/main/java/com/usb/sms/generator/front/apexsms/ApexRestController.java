package com.usb.sms.generator.front.apexsms;

import org.springframework.http.ResponseEntity;

/**
 * Rest interface for controlling Apex sms generator service
 */
public interface ApexRestController {
    /**
     * Enable Apex sms generator
     */
    ResponseEntity<String> enableApexService();

    /**
     * Disable Apex sms generator
     */
    ResponseEntity<String> disableApexService();


    /**
     * Keep Messages in apex SMS Pool
     */
    ResponseEntity<String> keepSmsInApexSmsPool(Boolean keep);

    /**
     * Clears last read APEX transactionId
     */
    ResponseEntity<String> clearLastReadTransactionId();

    /**
     * Get restricted accounts
     */
    ResponseEntity<String> getRestrictedAccounts();

    /**
     * Set restricted accounts
     */
    ResponseEntity<String> setRestrictedAccounts(String accountsList);



}
