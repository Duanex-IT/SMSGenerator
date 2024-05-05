package com.usb.sms.generator;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableSwagger
@EnableWebMvc
@EnableWebSecurity
@ComponentScan({"com.usb.sms.generator.front.apexsms"})
public class SmsGeneratorWebConfig extends WebSecurityConfigurerAdapter {

    @Value( "${apex.sms.api.user}" )
    private String apiUser;

    @Value( "${apex.sms.api.password}" )
    private String apiPassword;

    private SpringSwaggerConfig springSwaggerConfig;

    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }

    @Bean //Don't forget the @Bean annotation
    public SwaggerSpringMvcPlugin customImplementation(){
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
                .apiInfo(apiInfo())
                .apiVersion("v1");


    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "SMS Generator REST API",
                "Rest API for SMS Generator",
                "Terms of service",
                "plekhov.s@usbbank.com.ua",
                "commercial",
                ""
        );
        return apiInfo;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.inMemoryAuthentication()
                .withUser(apiUser).password(apiPassword).roles("API");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
         .authorizeRequests()
                .antMatchers("/apexsms/**").hasRole("API")
         .and()
     .httpBasic()
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);;

    }

}
