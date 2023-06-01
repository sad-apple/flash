package com.example.smartdoc.controller;

import com.example.smartdoc.dto.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test
 * 用户信息管理
 * @author zsp
 * @date 2023/6/1 13:42
 */
@RestController
@RequestMapping("/users")
public class UserControler {

    /**
     * 用户新增
     * @param user 用户信息
     * @return
     */
    @PostMapping("/")
    public void create(@RequestBody User user) {
        System.out.println(user);
    }

    /**
     * 用户编辑
     * @param id 用户id
     * @param user 用户信息
     */
    @PutMapping("/{id}")
    public void update(@PathVariable String id, @RequestBody User user) {
        System.out.println(id);
        System.out.println(user);
    }

    /**
     * 查询用户详情
     * @param id 主键
     */
    @GetMapping("/{id}")
    public User update(@PathVariable String id) {
        System.out.println(id);
        return new User(id);
    }

    /**
     * 删除用户
     * @param id 主键
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        System.out.println(id);
    }
}
