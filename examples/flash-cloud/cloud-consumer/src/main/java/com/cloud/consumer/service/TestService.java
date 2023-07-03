package com.cloud.consumer.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zsp
 * @date 2023/6/25 20:00
 */
@Slf4j
public class TestService {

    private String name;

    public TestService(String name) {
        this.name = name;
    }

    public void echo() {
        log.info("name={}", name);
    }
}
