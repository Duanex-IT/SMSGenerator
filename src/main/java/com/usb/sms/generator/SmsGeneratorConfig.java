package com.usb.sms.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan({"com.usb.sms.generator.service.scrooge", "com.usb.sms.generator.utils"})
@Import({ApexSmsGeneratorConfig.class, SmsGeneratorWebConfig.class, DmpPostgresConfig.class})
@EnableScheduling
@PropertySources({
        @PropertySource("classpath:sms.generator.properties"),
        @PropertySource(value = "file:${catalina.base}/../config/sms.generator.properties", ignoreResourceNotFound = true)
})
public class SmsGeneratorConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer smsGeneratorPropertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(30 * 1000);
        factory.setConnectTimeout(30 * 1000);
        RestTemplate rt = new RestTemplate(factory);
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());
        return rt;
    }



}
