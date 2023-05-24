package com.ndsc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author zsp
 * @date 2023/4/4 9:11
 */
@Slf4j
@Component
public class AppCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("完成启动预加载。。。");
        log.error("error日志");
        log.debug("debug日志");
        log.warn("warn日志");
    }

}
