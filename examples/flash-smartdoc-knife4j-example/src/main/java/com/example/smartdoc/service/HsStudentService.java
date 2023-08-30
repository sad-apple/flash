package com.example.smartdoc.service;

import org.springframework.stereotype.Service;

/**
 * @author zsp
 * @date 2023/8/29 14:20
 */
public class HsStudentService implements StudentService {

    @Override
    public String getType() {
        return "hs";
    }

}
