package com.ndsc.rabbitmq.producer.sender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/26 17:11
 */
@Component
public class TopicMoreSender extends TopicSender {

    public TopicMoreSender(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    @Override
    public void send(String routingKey, Object msg) {
        super.send(routingKey, msg);
    }

}
