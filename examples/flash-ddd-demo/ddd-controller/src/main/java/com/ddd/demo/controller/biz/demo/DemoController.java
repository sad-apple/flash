package com.ddd.demo.controller.biz.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsp
 * @date 2023/7/10 9:43
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @PostMapping
    public void create() {

    }
}
