package com.sensitive.controller;

import com.sensitive.dto.TestDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsp
 * @date 2023/6/6 15:42
 */
@RestController
public class TestController {

    @GetMapping("/get")
    public TestDto getTest() {
        TestDto testDto = new TestDto("18665548647");
        return testDto;
    }

}
