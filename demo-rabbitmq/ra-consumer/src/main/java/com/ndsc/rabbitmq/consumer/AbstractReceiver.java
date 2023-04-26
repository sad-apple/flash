package com.ndsc.rabbitmq.consumer;

/**
 * @author zsp
 * @date 2023/4/26 17:00
 */
public abstract class AbstractReceiver implements IReceiver {

    @Override
    public void receive(Object msg) {
        process(msg);
    }

    abstract void process(Object msg);

}
