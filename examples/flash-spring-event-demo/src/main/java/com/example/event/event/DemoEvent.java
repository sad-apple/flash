package com.example.event.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zsp
 * @date 2023/8/29 15:20
 */
public class DemoEvent extends ApplicationEvent {

    private String name;

    public DemoEvent(String name) {
        super(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
