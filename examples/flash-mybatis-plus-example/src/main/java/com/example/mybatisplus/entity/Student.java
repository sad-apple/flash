package com.example.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.flash.common.sensitive.annotations.SensitiveFormat;
import com.flash.common.sensitive.constant.SensitiveType;
import com.flash.mybatis.annotation.FieldSensitive;
import com.flash.mybatis.entity.BaseEntity;
import com.flash.mybatis.handlers.EncryptTypeHandler;
import lombok.Data;

/**
 * 学生信息
 * @author zsp
 * @date 2023/6/1 15:17
 */
@Data
@TableName(value = "t_student", autoResultMap = true)
public class Student extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 真实名称
     */
    private String realname;

    @FieldSensitive(SensitiveType.IDCARD_STRONG)
//    @SensitiveFormat(SensitiveType.IDCARD_STRONG)
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String idCard;

    /**
     * 年龄
     */
    private int age;

    /**
     * 性别
     */
    private String gender;


}
