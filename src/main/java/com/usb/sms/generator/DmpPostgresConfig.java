package com.usb.sms.generator;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

@Configuration
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@ComponentScan({"com.usb.sms.generator.service.dmp"})
@MapperScan(value="com.usb.sms.generator.mapper")
public class DmpPostgresConfig {

    final static private Logger logger = LoggerFactory.getLogger(DmpPostgresConfig.class);

    @Value("${dmpdbaddress}")
    private String dmpdbaddress;
    @Value("${dmpDbuser}")
    private String dmpdbuser;
    @Value("${dmpDbpassword}")
    private String dmpdbpassword;

    @Bean
    public SqlSessionFactory dmpSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dmpDataSource());
        return sqlSessionFactory.getObject();
    }


    @Bean
    public DataSource dmpDataSource() throws PropertyVetoException, SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("org.postgresql.Driver");
        dataSource.setJdbcUrl(dmpdbaddress);
        dataSource.setUser(dmpdbuser);
        dataSource.setLoginTimeout(100);
        dataSource.setMinPoolSize(5);
        dataSource.setMaxPoolSize(40);
        dataSource.setCheckoutTimeout(5000);
        dataSource.setUnreturnedConnectionTimeout(50);
        dataSource.setDebugUnreturnedConnectionStackTraces(true);
        dataSource.setAcquireRetryAttempts(2);
        dataSource.setMaxConnectionAge(180);
        dataSource.setMaxIdleTime(60);
        dataSource.setBreakAfterAcquireFailure(false);
        dataSource.setTestConnectionOnCheckin(true);
        dataSource.setNumHelperThreads(10);
        dataSource.setPassword(dmpdbpassword);
//        dataSource.setConnectionCustomizerClassName("com.usb.esb.proxies.scrooge.util.UuidConnectionCustomizer");
        return dataSource;
    }

}
