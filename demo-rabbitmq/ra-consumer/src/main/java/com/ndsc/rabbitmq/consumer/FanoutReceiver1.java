package com.ndsc.rabbitmq.consumer;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/27 8:59
 */
@Component
@RabbitListener(queues = Constant.FANOUT_QUEUE_1, group = "fanout1")
public class FanoutReceiver1 extends AbstractReceiver {

    @Override
    void process(Object msg) {
        System.out.println(Constant.FANOUT_QUEUE_1 + ",消费者收到消息  : " + msg.toString());
    }

}
