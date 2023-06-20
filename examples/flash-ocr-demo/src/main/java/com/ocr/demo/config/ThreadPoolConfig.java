package com.ocr.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zsp
 * @date 2023/6/20 15:54
 */
@Configuration
public class ThreadPoolConfig {

    @Autowired
    private TaskExecutorBuilder builder;

    @Bean
    public ThreadPoolTaskExecutor ocrTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = builder.build();
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }

}
