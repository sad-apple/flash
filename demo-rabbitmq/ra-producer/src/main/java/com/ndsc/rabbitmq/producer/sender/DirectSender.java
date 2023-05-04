package com.ndsc.rabbitmq.producer.sender;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/26 16:50
 */
@Component
public class DirectSender extends AbstractSender {

    public DirectSender(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    @Override
    public void send(String routingKey, Object msg) {
        rabbitTemplate.convertAndSend(Constant.DIRECT_EXCHANGE, routingKey, msg);
    }

    @Override
    public void send(Object msg) {
        send(Constant.DIRECT_ROUTING, msg);
    }

}
