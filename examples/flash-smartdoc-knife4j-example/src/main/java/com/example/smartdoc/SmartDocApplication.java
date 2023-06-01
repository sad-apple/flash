package com.example.smartdoc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zsp
 * @date 2023/6/1 11:27
 */
@Slf4j
@SpringBootApplication
public class SmartDocApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(SmartDocApplication.class, args);
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
}
