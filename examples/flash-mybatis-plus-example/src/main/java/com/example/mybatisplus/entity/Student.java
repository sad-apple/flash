package com.example.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.flash.common.constant.SensitiveType;
import com.flash.mybatis.annotation.FieldSensitive;
import lombok.Data;

/**
 * 学生信息
 * @author zsp
 * @date 2023/6/1 15:17
 */
@Data
@TableName("t_student")
public class Student {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 真实名称
     */
    private String realname;

    @FieldSensitive(SensitiveType.IDCARD_STRONG)
    private Integer idCard;

    /**
     * 年龄
     */
    private int age;

    /**
     * 性别
     */
    private String gender;


}
