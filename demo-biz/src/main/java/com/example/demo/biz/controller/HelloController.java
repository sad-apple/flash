package com.example.demo.biz.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Api(tags = "hello world")
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/demo")
    public String download(HttpServletResponse response){
        return "hello world";
    }
}
