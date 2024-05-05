package com.usb.sms.generator.service.apex.model;

public class Transaction {
    private String phone;
    private int actionId;
    private long accountId;
    private Double amount;
    private String currency;
    private String operDate;
    private String operMonth;
    private String smsText;

    public Transaction(String phone, int actionId, long accountId, Double amount, String currency, String operDate, String operMonth) {
        this.phone = phone;
        this.actionId = actionId;
        this.amount = amount;
        this.currency = currency;
        this.operDate = operDate;
        this.accountId = accountId;
        this.operMonth = operMonth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public String getOperMonth() {
        return operMonth;
    }

    public void setOperMonth(String operMonth) {
        this.operMonth = operMonth;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "phone='" + phone + '\'' +
                ", actionId=" + actionId +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", operDate='" + operDate + '\'' +
                '}';
    }
}
