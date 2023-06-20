package com.example.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mybatisplus.entity.Student;
import com.example.mybatisplus.mapper.StudentMapper;
import org.springframework.stereotype.Service;

/**
 * @author zsp
 * @date 2023/6/20 10:42
 */
@Service
public class StudentService extends ServiceImpl<StudentMapper, Student> {

    public Student findById(String id) {
        return getBaseMapper().findById(id);
    }

}
