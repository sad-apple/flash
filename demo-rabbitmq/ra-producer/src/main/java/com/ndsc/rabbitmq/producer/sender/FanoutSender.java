package com.ndsc.rabbitmq.producer.sender;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/27 15:51
 */
@Component
public class FanoutSender extends AbstractSender {

    public FanoutSender(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    @Override
    public void send(Object msg) {
        rabbitTemplate.convertAndSend(Constant.FANOUT_EXCHANGE, null, msg);
    }

}
