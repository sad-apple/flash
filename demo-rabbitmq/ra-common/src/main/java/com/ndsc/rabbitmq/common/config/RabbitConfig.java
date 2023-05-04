package com.ndsc.rabbitmq.common.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsp
 * @date 2023/4/28 10:15
 */
@Configuration
public class RabbitConfig {
    @Bean
    public MessageConverter messageconverter() {
        return new Jackson2JsonMessageConverter();
    }
}
