package com.ndsc.rabbitmq.producer.sender;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/26 17:10
 */
@Component
public class TopicOneSender extends TopicSender {

    public TopicOneSender(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    @Override
    public void send(Object msg) {
        super.send(Constant.TOPIC_ROUTING_ONE, msg);
    }

}
