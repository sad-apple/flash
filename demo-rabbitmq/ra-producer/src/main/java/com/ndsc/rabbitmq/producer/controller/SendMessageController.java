package com.ndsc.rabbitmq.producer.controller;

import com.ndsc.rabbitmq.producer.TestService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zsp
 * @date 2023/4/24 13:58
 */
@RestController
public class SendMessageController {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    private TestService testService;

    @GetMapping("{testService.getUrl('dfe')}")
//    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        System.out.println(testService.getUrl("dfe"));

        for (int i = 0; i < 10; i++) {
            String messageId = String.valueOf(UUID.randomUUID());
            String messageData = "test message, hello!";
            String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> map = new HashMap<>();
            map.put("messageId", messageId);
            map.put("messageData", messageData);
            map.put("createTime", createTime);
            map.put("sort", i);
            //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
            rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        }


        return "ok";
    }

}
