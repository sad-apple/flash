package com.ndsc.rabbitmq.consumer;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/26 17:02
 */
@Component
@RabbitListener(queues = Constant.DIRECT_QUEUE)
public class DirectReceiver extends AbstractReceiver {

    @Override
    void process(Object msg) {
        System.out.println("DirectReceiver消费者收到消息  : " + msg.toString());
    }

}
