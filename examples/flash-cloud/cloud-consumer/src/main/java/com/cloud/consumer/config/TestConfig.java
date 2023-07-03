package com.cloud.consumer.config;

import com.cloud.consumer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsp
 * @date 2023/6/25 19:59
 */
@Configuration
public class TestConfig {

    @Autowired
    private TestProperties properties;

    @Bean
    public TestService testService() {
        return new TestService(properties.getName());
    }

}
