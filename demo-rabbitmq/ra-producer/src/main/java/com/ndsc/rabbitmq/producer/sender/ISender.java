package com.ndsc.rabbitmq.producer.sender;

/**
 * @author zsp
 * @date 2023/4/26 16:46
 */
public interface ISender {

    void send(Object msg);

}
