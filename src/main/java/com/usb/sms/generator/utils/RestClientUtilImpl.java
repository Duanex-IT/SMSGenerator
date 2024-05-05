package com.usb.sms.generator.utils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RestClientUtilImpl implements RestClientUtil {

    private final static org.slf4j.Logger log = LoggerFactory.getLogger(RestClientUtilImpl.class);

    @Autowired
    RestTemplate rest;

    @Value( "${esb.rest.url}" )
    private String url;

    @Override
    public <T> T httpPost(String methodUrl, Object requestBody, Class<T> responseClass) {
        log.debug("Calling "+(url+methodUrl));
        return rest.postForObject(url+methodUrl, requestBody, responseClass);
    }

    @Override
    public <T> T httpGet(String methodUrl, Map<String, Object> params, Class<T> responseClass) {
        log.debug("Calling "+(url+methodUrl));
        return rest.getForObject(url+methodUrl, responseClass, params);
    }

}
