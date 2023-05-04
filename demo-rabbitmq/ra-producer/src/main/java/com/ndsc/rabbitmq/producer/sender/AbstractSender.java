package com.ndsc.rabbitmq.producer.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author zsp
 * @date 2023/4/26 16:49
 */
public abstract class AbstractSender implements ISender {

    RabbitTemplate rabbitTemplate;

    public AbstractSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(String routingKey, Object msg){
        // 这个方法需要重写
        send(msg);
    }

}
