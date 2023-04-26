package com.ndsc.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;

import java.util.Map;

/**
 * @author zsp
 * @date 2023/4/26 16:59
 */
public interface IReceiver {

    @RabbitHandler
    void receive(Object msg);

}
