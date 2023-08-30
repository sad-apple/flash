package com.example.event.listener;

import com.example.event.event.DemoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author zsp
 * @date 2023/8/29 15:24
 */
@Component
public class DemoListener {


    @EventListener
    public void listener1(DemoEvent event) {
        System.out.println("listener1:" + event.getName());
    }

    @Async
    @EventListener
    public void listener2(DemoEvent event) {
        System.out.println("listener2:" + event.getName());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void listener3(DemoEvent event) {
        System.out.println("listener3:" + event.getName());
    }

}
