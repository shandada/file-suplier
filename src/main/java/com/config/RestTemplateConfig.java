package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @author: Endless 2019/11/29 15:48
 * RestTemplate工具类，主要用来提供RestTemplate对象
 */

/**
 *   @Configuration  这个注解作用，启动服务 此类可以被Spring扫描
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 创建RestTemplate对象，将RestTemplate对象的生命周期的管理交给Spring
     *
     * @return
     */
    @Bean
    public RestTemplate add() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        //支持中文编码
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}

