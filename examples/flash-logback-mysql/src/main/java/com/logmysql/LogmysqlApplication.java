package com.logmysql;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zsp
 * @date 2023/6/26 19:24
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
public class LogmysqlApplication implements CommandLineRunner {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(LogmysqlApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        path = path == null ? "" : path;
        log.info("\n----------------------------------------------------------\n\t" +
                 "Application demo is running! Access URLs:\n\t" +
                 "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                 "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                 "Swagger文档中文测试: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                 "----------------------------------------------------------");
    }

    @Override
    public void run(String... args) throws Exception {

    }

    private final static AtomicInteger count = new AtomicInteger(0);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Scheduled(cron = "*/1 * * * * ?")
    public void scheduler() {
        taskExecutor.execute(() -> {
            for (int i = 0; i < 5000; i++) {
                log.info("code:{}, date:{}, seq:{}", UUID.randomUUID(), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), count.incrementAndGet());
            }
        });

    }

    @Scheduled(cron = "*/1 * * * * ?")
    public void scheduler2() {
        for (int i = 0; i < 2200; i++) {
            log.info("code:{}, date:{}, seq:{}, random1:{}, random2:{}", UUID.randomUUID(), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                     count.incrementAndGet(), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        }
    }

    @Scheduled(cron = "*/1 * * * * ?")
    public void scheduler3() {
        for (int i = 0; i < 2200; i++) {
            log.info("code:{}, date:{}, seq:{}, random1:{}, random2:{}", UUID.randomUUID(), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                     count.incrementAndGet(), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        }
    }

    @Scheduled(cron = "*/1 * * * * ?")
    public void scheduler4() {
        for (int i = 0; i < 1000; i++) {
            log.info("code:{}, date:{}, seq:{}, random1:{}, random2:{}", UUID.randomUUID(), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                     count.incrementAndGet(), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        }
    }

    @Scheduled(cron = "*/1 * * * * ?")
    public void scheduler5() {
        for (int i = 0; i < 1000; i++) {
            log.info("code:{}, date:{}, seq:{}, random1:{}, random2:{}", UUID.randomUUID(), LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                     count.incrementAndGet(), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        }
    }
}
