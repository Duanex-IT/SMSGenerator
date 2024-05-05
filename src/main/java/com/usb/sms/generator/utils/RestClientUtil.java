package com.usb.sms.generator.utils;

import java.util.Map;

public interface RestClientUtil {

    /**
     * POST to ESB
     * @param methodUrl like "auth/login"
     * @param requestBody
     * @param responseClass
     * @param <T>
     * @return
     */
    <T> T httpPost(String methodUrl, Object requestBody, Class<T> responseClass);

    <T> T httpGet(String methodUrl, Map<String, Object> params, Class<T> responseClass);
}
