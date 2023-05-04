package com.ndsc.rabbitmq.producer.config;

import com.ndsc.rabbitmq.common.Constant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsp
 * @date 2023/4/27 15:40
 */
@Configuration
public class FanoutRabbitConfig {
    @Bean
    public Queue fanoutQueue1() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(Constant.FANOUT_QUEUE_1);
    }
    @Bean
    public Queue fanoutQueue2() {
        return new Queue(Constant.FANOUT_QUEUE_2);
    }

    @Bean
    public Queue fanoutQueue3() {
        return new Queue(Constant.FANOUT_QUEUE_3);
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(Constant.FANOUT_EXCHANGE);
    }

    @Bean
    Binding bindFanout1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }
    @Bean
    Binding bindFanout2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }
    @Bean
    Binding bindFanout3() {
        return BindingBuilder.bind(fanoutQueue3()).to(fanoutExchange());
    }
}
