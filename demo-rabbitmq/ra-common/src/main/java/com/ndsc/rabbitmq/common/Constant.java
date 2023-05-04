package com.ndsc.rabbitmq.common;

/**
 * @author zsp
 * @date 2023/4/24 14:43
 */
public class Constant {

    public static final String DIRECT_QUEUE = "direct-queue";
    public static final String DIRECT_EXCHANGE = "direct-exchange";
    public static final String DIRECT_ROUTING = "direct-routing";

    public static final String TOPIC_QUEUE = "topic-queue";
    public static final String TOPIC_EXCHANGE = "topic-exchange";
    public static final String TOPIC_ROUTING_ONE = "topic.one";
    public static final String TOPIC_ROUTING_MORE = "topic.more.#";

    public static final String FANOUT_QUEUE_1 = "fanout-queue-1";
    public static final String FANOUT_QUEUE_2 = "fanout-queue-2";
    public static final String FANOUT_QUEUE_3 = "fanout-queue-3";
    public static final String FANOUT_EXCHANGE = "fanout-exchange";

}
