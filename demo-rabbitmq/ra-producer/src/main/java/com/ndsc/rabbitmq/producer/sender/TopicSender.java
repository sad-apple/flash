package com.ndsc.rabbitmq.producer.sender;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/26 17:10
 */
@Component
public class TopicSender extends AbstractSender {

    public TopicSender(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    @Override
    public void send(String routingKey, Object msg) {
        rabbitTemplate.convertAndSend(Constant.TOPIC_EXCHANGE, routingKey, msg);
    }

    @Override
    public void send(Object msg) {
        send(Constant.TOPIC_ROUTING_MORE, msg);
    }

}
