package com.usb.sms.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@ComponentScan({"com.usb.sms.generator.service.apex"})
/*
@PropertySources({
        @PropertySource("classpath:apexproxy.properties"),
        @PropertySource(value = "file:/opt/esb/config/apexproxy.properties", ignoreResourceNotFound = true)
})
*/
public class ApexSmsGeneratorConfig implements SchedulingConfigurer{
    final static private Logger logger = LoggerFactory.getLogger(ApexSmsGeneratorConfig.class);

    @Value("${apex-url}")
    private String apexDburl = "10.168.0.11:1521:apexline";
    @Value("${apex-user}")
    private String apexDbuser = "esb";
    @Value("${apex-password}")
    private String apexDbPassword = "APEXesbLine";

    @Value("${apex.sms.config.db.url}")
    private String configDburl = "jdbc:oracle:thin:@10.11.5.31:1521/esbdev";
    @Value("${apex.sms.config.db.user}")
    private String configDbuser = "esb";
    @Value("${apex.sms.config.db.password}")
    private String configDbPassword = "esb";

    @Bean
    public static PropertySourcesPlaceholderConfigurer apexSmsGeneratorPropertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Bean
    public ThreadPoolTaskExecutor sendSmsThreadExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(1);
        executor.setCorePoolSize(1);
        return executor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(threadPoolTaskExecutor());
    }
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskExecutor() {
        ThreadPoolTaskScheduler t = new ThreadPoolTaskScheduler();
        t.setPoolSize(1);
        return t;

    }

    @Bean
    public DataSource apexDataSource() {
        logger.info("Creating APEX Datasource using url: {}", apexDburl);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@"+apexDburl);
        dataSource.setUsername(apexDbuser);
        dataSource.setPassword(apexDbPassword);
        return dataSource;
    }


    @Bean
    public JdbcTemplate apexJdbcTemplate(){
        return new JdbcTemplate(apexDataSource());
    }

    @Bean
    public DataSource configApexSmsDataSource() {
        logger.info("Creating Config Datasource using url: {}", configDburl);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl(configDburl);
        dataSource.setUsername(configDbuser);
        dataSource.setPassword(configDbPassword);
        return dataSource;
    }

    @Qualifier("apexSmsTransactionManager")
    @Bean
    public PlatformTransactionManager apexSmsTransactionManager(){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(configApexSmsDataSource());
        return dataSourceTransactionManager;
    }


    @Bean
    public JdbcTemplate configApexSmsJdbcTemplate(){
        return new JdbcTemplate(configApexSmsDataSource());
    }
}
