package com.ndsc.biz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ndsc.biz.dao.entity.User;
import com.ndsc.biz.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsp
 * @date 2023/4/4 16:36
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public boolean insert(@RequestBody User user) {
        userService.save(user);
        return true;
    }

    @GetMapping("/users/user/page")
    public Page<User> page(Integer page, Integer size) {
        return userService.page(Page.of(page, size), new LambdaQueryWrapper<User>().orderByDesc(User::getCreateTime));
    }

    @DeleteMapping("/users/{id}")
    public boolean delete(@PathVariable Long id) {
        return userService.removeById(id);
    }

    @PutMapping("/users")
    public boolean update(@RequestBody User user) {
        return userService.updateById(user);
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

}
