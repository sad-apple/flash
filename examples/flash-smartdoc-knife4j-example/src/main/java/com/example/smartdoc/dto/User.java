package com.example.smartdoc.dto;

import lombok.Data;

/**
 * 用户信息
 * @author zsp
 * @date 2023/6/1 13:43
 */
@Data
public class User {

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 年龄
     */
    private int age;

    /**
     * 地址
     */
    private String address;

    public User(String id) {
        this.id = id;
    }

}
