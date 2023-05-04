package com.ndsc.rabbitmq.producer.controller;

import com.ndsc.rabbitmq.producer.sender.DirectSender;
import com.ndsc.rabbitmq.producer.sender.FanoutSender;
import com.ndsc.rabbitmq.producer.sender.TopicMoreSender;
import com.ndsc.rabbitmq.producer.sender.TopicOneSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zsp
 * @date 2023/4/24 13:58
 */
@RestController
@Slf4j
public class SendMessageController {

    @Autowired
    DirectSender directSender;
    @Autowired
    private TopicOneSender topicOneSender;
    @Autowired
    private TopicMoreSender topicMoreSender;
    @Autowired
    private FanoutSender fanoutSender;

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        for (int i = 0; i < 1000; i++) {
            String messageId = String.valueOf(UUID.randomUUID());
            String messageData = "test message, hello!";
            String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> map = new HashMap<>();
            map.put("messageId", messageId);
            map.put("messageData", messageData);
            map.put("createTime", createTime);
            map.put("sort", i);
            //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
            directSender.send(map);
        }
        return "ok";
    }

    @GetMapping("/sendTopic")
    public String sendTopic() {
        for (int i = 0; i < 1000; i++) {
            String messageId = String.valueOf(UUID.randomUUID());
            String messageData = "test message, hello!";
            String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> map = new HashMap<>();
            map.put("messageId", messageId);
            map.put("messageData", messageData);
            map.put("createTime", createTime);
            map.put("sort", i);
            //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
            topicOneSender.send(map);
        }
        return "ok";
    }

    @GetMapping("/sendTopicMore")
    public String sendTopicMore() {
        for (int i = 0; i < 1000; i++) {
            String messageId = String.valueOf(UUID.randomUUID());
            String messageData = "test message, hello!";
            String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> map = new HashMap<>();
            map.put("messageId", messageId);
            map.put("messageData", messageData);
            map.put("createTime", createTime);
            map.put("sort", i);
            //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
            topicMoreSender.send("topic.more.apple", map);
            topicMoreSender.send("topic.more.pear", map);
        }
        return "ok";
    }

    @GetMapping("/sendFanout")
    public String sendFanout() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("sort", count.incrementAndGet());
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        Long id = 23L;
        fanoutSender.send("topic.more.apple", id);
        fanoutSender.send("topic.more.apple", map);
        return "ok";
    }

    private AtomicInteger count = new AtomicInteger(0);

    @Scheduled(cron = "0/10 * * * * ?")
//    @Scheduled(fixedRate = 1)
    public void testScheduler() {
        log.info("=========开始发送消息==========");
//        new Thread(this::sendTopic).start();
//        new Thread(this::sendDirectMessage).start();
        new Thread(this::sendFanout).start();
//        new Thread(this::sendTopicMore).start();
    }

}
