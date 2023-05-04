package com.ndsc.rabbitmq.consumer;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/27 8:59
 */
@Component
@RabbitListener(queues = Constant.FANOUT_QUEUE_2)
public class FanoutReceiver02 extends AbstractReceiver {

    @Override
    void process(Object msg) {
        System.out.println(Constant.FANOUT_QUEUE_2 + "02,消费者收到消息  : " + msg.toString());
    }

}
