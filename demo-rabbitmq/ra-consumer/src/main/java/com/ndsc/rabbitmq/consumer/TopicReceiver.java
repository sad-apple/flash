package com.ndsc.rabbitmq.consumer;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/27 8:59
 */
@Component
@RabbitListener(queues = Constant.TOPIC_QUEUE)
public class TopicReceiver extends AbstractReceiver {

    @Override
    void process(Object msg) {
        System.out.println("TopicReceiver消费者收到消息  : " + msg.toString());
    }

}
