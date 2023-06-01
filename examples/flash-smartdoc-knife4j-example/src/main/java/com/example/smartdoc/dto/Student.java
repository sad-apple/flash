package com.example.smartdoc.dto;

import lombok.Data;

/**
 * 学生信息
 * @author zsp
 * @date 2023/6/1 15:17
 */
@Data
public class Student {

    private String id;

    /**
     * 真实名称
     */
    private String realname;

    /**
     * 年龄
     */
    private int age;

    /**
     * 性别
     */
    private String gender;

    public Student(String id) {
        this.id = id;
    }

}
