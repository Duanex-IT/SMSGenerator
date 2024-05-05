package com.usb.sms.generator.service.apex.model;

import java.util.HashSet;
import java.util.Set;

public class Config {
    private long lastTransactionId;
    private boolean isEnabled;
    private boolean keepSmsPool = true;
    private Set<String> accountsToInclude = new HashSet<>();

    public Config() {
        isEnabled=false;
    }

    public long getLastTransactionId() {
        return lastTransactionId;
    }

    public void setLastTransactionId(long lastTransactionId) {
        this.lastTransactionId = lastTransactionId;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isKeepSmsPool() {
        return keepSmsPool;
    }

    public void setKeepSmsPool(boolean keepSmsPool) {
        this.keepSmsPool = keepSmsPool;
    }

    public Set<String> getAccountsToInclude() {
        return accountsToInclude;
    }

    public void setAccountsToInclude(Set<String> accountsToInclude) {
        this.accountsToInclude.addAll(accountsToInclude);
    }

    @Override
    public String toString() {
        return "Config{" +
                "lastTransactionId=" + lastTransactionId +
                ", isEnabled=" + isEnabled +
                ", keepSmsPool=" + keepSmsPool +
                '}';
    }
}
