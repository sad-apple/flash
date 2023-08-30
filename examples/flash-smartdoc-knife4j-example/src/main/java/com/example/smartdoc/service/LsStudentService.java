package com.example.smartdoc.service;

import org.springframework.stereotype.Service;

/**
 * @author zsp
 * @date 2023/8/29 14:21
 */
public class LsStudentService implements StudentService {

    @Override
    public String getType() {
        return "ls";
    }

}
