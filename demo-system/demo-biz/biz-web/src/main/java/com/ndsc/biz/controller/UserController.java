package com.ndsc.biz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ndsc.biz.dao.entity.SysUser;
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
    public boolean insert(@RequestBody SysUser sysUser) {
        userService.save(sysUser);
        return true;
    }

    @GetMapping("/users/user/page")
    public Page<SysUser> page(Integer page, Integer size) {
        return userService.page(Page.of(page, size), new LambdaQueryWrapper<SysUser>().orderByDesc(SysUser::getCreateTime));
    }

    @DeleteMapping("/users/{id}")
    public boolean delete(@PathVariable Long id) {
        return userService.removeById(id);
    }

    @PutMapping("/users")
    public boolean update(@RequestBody SysUser sysUser) {
        return userService.updateById(sysUser);
    }

    @GetMapping("/users/{id}")
    public SysUser getById(@PathVariable Long id) {
        return userService.getById(id);
    }

}
