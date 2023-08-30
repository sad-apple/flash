package com.example.event.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.event.entity.Student;
import org.apache.ibatis.annotations.Param;

/**
 * @author zsp
 * @date 2023/6/20 10:50
 */
public interface StudentMapper extends BaseMapper<Student> {

    Student findById(@Param("id") String id);

}
