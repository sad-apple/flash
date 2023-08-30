package com.example.event.pusher;

import com.example.event.event.DemoEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/8/29 15:25
 */
@Component
public class DemoPusher implements ApplicationEventPublisherAware {

    ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void success(String name) {
        applicationEventPublisher.publishEvent(new DemoEvent(name));
    }
}
