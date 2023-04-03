package com.example.demo.biz.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * @author zsp
 * @date 2023/4/3 14:32
 */
@Slf4j
@Configuration
public class RedisLockConfiguration {

    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        String registryKey = "demo-redis-lock-tool";
        log.info("配置redis lock，前缀：{}", registryKey);
        return new RedisLockRegistry(redisConnectionFactory, registryKey);
    }

}
