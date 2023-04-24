package com.ndsc.rabbitmq.producer;

import org.springframework.stereotype.Service;

/**
 * @author zsp
 * @date 2023/4/24 15:23
 */
@Service
public class TestService {

    public String getUrl(String code) {
        return "/sendDirectMessage1";
    }
}
