package com.cloud.consumer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/6/25 19:13
 */
@Component
@Data
@ConfigurationProperties("consumer")
public class TestProperties {

    private String name;
}
