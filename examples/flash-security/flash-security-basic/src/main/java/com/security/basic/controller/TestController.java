package com.security.basic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsp
 * @date 2023/7/21 10:59
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("")
    public String test() {
        return "hello world";
    }

}
