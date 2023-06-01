package com.example.mybatisplus.controller.student;

import com.example.mybatisplus.entity.Student;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 学生信息管理
 * @author zsp
 * @date 2023/6/1 13:42
 */
@RestController
@RequestMapping("/students")
public class StudentControler {

    /**
     * 学生新增
     * @param student 学生信息
     * @return
     */
    @PostMapping("/")
    public void create(@RequestBody Student student) {
        System.out.println(student);
    }

    /**
     * 学生编辑
     * @param id 学生id
     * @param student 学生信息
     */
    @PutMapping("/{id}")
    public void update(@PathVariable String id, @RequestBody Student student) {
        System.out.println(id);
        System.out.println(student);
    }

    /**
     * 查询学生详情
     * @param id 主键
     */
    @GetMapping("/{id}")
    public Student update(@PathVariable String id) {
        System.out.println(id);
        return new Student();
    }

    /**
     * 删除学生
     * @param id 主键
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        System.out.println(id);
    }
}
