package com.ndsc.biz.mongo.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.ndsc.biz.mongo.MongodUserService;
import com.ndsc.biz.mongo.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author zsp
 * @date 2023/4/12 11:19
 */
@Api(tags = "mongodb测试")
@RequestMapping("/mongodb")
@RestController
public class MongoController {

    @Autowired
    private MongodUserService mongodUserService;

    @ApiOperationSupport(order = 12001)
    @ApiOperation(value = "新增用户")
    @PostMapping("/users")
    public boolean insert(@RequestBody User user) {
        mongodUserService.save(user);
        return true;
    }

    @ApiOperationSupport(order = 12002)
    @ApiOperation(value = "分页查询")
    @GetMapping("/users/user/page")
    public Page<User> page(Integer page, Integer size) {
        return mongodUserService.page(page, size);
    }

    @ApiOperationSupport(order = 12003)
    @ApiOperation(value = "删除")
    @DeleteMapping("/users/{id}")
    public boolean delete(@PathVariable String id) {
        return mongodUserService.removeById(id);
    }

    @ApiOperationSupport(order = 12004)
    @ApiOperation(value = "修改")
    @PutMapping("/users")
    public boolean update(@RequestBody User user) {
        return mongodUserService.updateById(user);
    }

    @ApiOperationSupport(order = 12005)
    @ApiOperation(value = "查询")
    @GetMapping("/users/{id}")
    public User getById(@PathVariable String id) {
        return mongodUserService.selectById(id);
//        return userService.getById(id);
    }

    @ApiOperationSupport(order = 12005)
    @ApiOperation(value = "文件上传")
    @PostMapping("/users/{id}/uploadAvatar")
    public void uploadAvatar(@PathVariable String id, @RequestParam(value = "image") MultipartFile file) throws IOException {
        mongodUserService.uploadAvatar(id, file);
//        return userService.getById(id);
//        System.out.println(file.getName());
    }
}
