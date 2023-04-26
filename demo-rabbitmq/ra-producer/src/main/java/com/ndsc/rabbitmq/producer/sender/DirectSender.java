package com.ndsc.rabbitmq.producer.sender;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/26 16:50
 */
@Component
public class DirectSender extends AbstractSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(Object msg) {
        rabbitTemplate.convertAndSend(Constant.DIRECT_EXCHANGE, Constant.DIRECT_ROUTING, msg);
    }

}
