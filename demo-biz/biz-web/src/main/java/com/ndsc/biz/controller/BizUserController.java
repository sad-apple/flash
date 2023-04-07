package com.ndsc.biz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ndsc.biz.dao.entity.BizUser;
import com.ndsc.biz.service.user.BizUserService;
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
public class BizUserController {

    @Autowired
    private BizUserService userService;

    @PostMapping("/biz-users")
    public boolean insert(@RequestBody BizUser sysUser) {
        userService.save(sysUser);
        return true;
    }

    @GetMapping("/biz-users/user/page")
    public Page<BizUser> page(Integer page, Integer size) {
        return userService.page(Page.of(page, size), new LambdaQueryWrapper<BizUser>().orderByDesc(BizUser::getCreateTime));
    }

    @DeleteMapping("/biz-users/{id}")
    public boolean delete(@PathVariable Long id) {
        return userService.removeById(id);
    }

    @PutMapping("/biz-users")
    public boolean update(@RequestBody BizUser sysUser) {
        return userService.updateById(sysUser);
    }

    @GetMapping("/biz-users/{id}")
    public BizUser getById(@PathVariable String id) {
        return userService.selectById(id);
//        return userService.getById(id);
    }

}
