package com.ndsc.biz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@Api(tags = "hello world")
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @ApiOperation(value = "demo")
    @GetMapping("/demo")
    public String download(HttpServletResponse response) {
        return "hello world";
    }

    @ApiOperation(value = "redisLock")
    @GetMapping("/redisLock")
    public String redisLock(HttpServletResponse response) throws InterruptedException {
        Lock obtain = redisLockRegistry.obtain("test_lock");
        if (obtain.tryLock(20, TimeUnit.SECONDS)) {
            try {
                log.info("进入锁");
                Thread.sleep(5000);
            }finally {
                obtain.unlock();
                log.info("解锁");
            }
        } else {
            log.info("获取锁失败");
        }
        return "hello world";
    }

}
