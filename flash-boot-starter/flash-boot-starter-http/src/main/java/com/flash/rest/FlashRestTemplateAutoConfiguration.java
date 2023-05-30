package com.flash.rest;

import com.flash.rest.interceptor.ClientHttpRequestInterceptorImpl;
import com.flash.rest.properties.RestProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsp
 * @date 2023/5/30 14:30
 */
@Configuration
@EnableConfigurationProperties(RestProperties.class)
public class FlashRestTemplateAutoConfiguration {


    @ConditionalOnMissingBean
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(RestProperties restProperties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(restProperties.getConnectTimeout());
        requestFactory.setReadTimeout(restProperties.getReadTimeout());
        return requestFactory;
    }

    @ConditionalOnMissingBean
    @Bean
    public RestTemplate flashRestTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        List<ClientHttpRequestInterceptor> interceptors=new ArrayList<>();
        interceptors.add(new ClientHttpRequestInterceptorImpl());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
