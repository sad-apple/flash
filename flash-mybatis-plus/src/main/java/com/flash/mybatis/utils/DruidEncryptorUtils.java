package com.flash.mybatis.utils;

import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;

/**
 * @author zsp
 * @date 2023/4/6 13:25
 * 数据源连接配置加密
 */
public class DruidEncryptorUtils {

    public static void main(String[] args) throws Exception {
        String username = CryptoUtils.encrypt("root");
        String password = CryptoUtils.encrypt("root");
        System.out.println("username=" + username);
        System.out.println("password=" + password);
    }
}
