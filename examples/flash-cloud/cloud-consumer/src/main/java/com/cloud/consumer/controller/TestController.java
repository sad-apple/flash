package com.cloud.consumer.controller;

import com.cloud.consumer.config.TestProperties;
import com.cloud.consumer.feign.CloudProviderClient;
import com.cloud.consumer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author zsp
 * @date 2023/6/25 16:10
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private CloudProviderClient cloudProviderClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TestProperties testProperties;
    @Autowired
    private TestService service;

    @GetMapping("/demo")
    public void demo() {
        String echo = cloudProviderClient.echo("hello world !");
        System.out.println(echo);
        System.out.println(testProperties.getName());
        service.echo();
    }

    @GetMapping("/demo1")
    public void demo1() {
        String echo = restTemplate.getForObject("http://cloud-provider/echo/sss", String.class);
        System.out.println(echo);
    }

}
