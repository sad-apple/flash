package com.ndsc.rabbitmq.consumer;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zsp
 * @date 2023/4/24 14:18
 */
@Component
@RabbitListener(queues = Constant.TEST_DIRECT_QUEUE)
public class DirectReceiver {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }

}