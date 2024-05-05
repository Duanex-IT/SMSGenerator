package com.usb.sms.generator.service.apex.model;

public class SmsTemplateConfig {
    private long id;
    private int actionId;
    private String description;
    private String template;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
