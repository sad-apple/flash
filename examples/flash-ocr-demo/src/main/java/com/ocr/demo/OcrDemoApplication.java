package com.ocr.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zsp
 * @date 2023/6/15 14:18
 */
@SpringBootApplication
@Slf4j
public class OcrDemoApplication implements CommandLineRunner {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(OcrDemoApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port", "8080");
        String path = env.getProperty("server.servlet.context-path");
        path = path == null ? "" : path;
        log.info("\n----------------------------------------------------------\n\t" +
                 "Application demo is running! Access URLs:\n\t" +
                 "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                 "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                 "Swagger doc url: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                 "----------------------------------------------------------");
    }

    @Override
    public void run(String... args) throws Exception {
        String threadNamePrefix = taskExecutor.getThreadNamePrefix();
        System.out.println("thread prefix: " + threadNamePrefix);
    }

}
