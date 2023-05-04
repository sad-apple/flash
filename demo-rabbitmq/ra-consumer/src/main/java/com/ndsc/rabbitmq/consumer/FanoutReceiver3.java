package com.ndsc.rabbitmq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndsc.rabbitmq.common.Constant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author zsp
 * @date 2023/4/27 8:59
 */
@Component
@RabbitListener(queues = Constant.FANOUT_QUEUE_3)
public class FanoutReceiver3 extends AbstractReceiver {

    @Override
    void process(Object msg) {
        Message message = (Message) msg;
        byte[] body = message.getBody();

        Object deserialize = SerializationUtils.deserialize(body);
//        ObjectMapper om = new ObjectMapper();
//        try {
//            Map map = om.readValue(body, Map.class);
//            System.out.println(map);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println(Constant.FANOUT_QUEUE_3 + ",消费者收到消息  : " + deserialize);
    }

    @RabbitHandler
    void test(Long num, Channel channel) {
        System.out.println("==========" + num + "==============");
        System.out.println("==========" + channel + "==============");
    }

    @RabbitHandler
    void test2(Map map) {
        System.out.println("==========" + map + "==============");
    }

}
