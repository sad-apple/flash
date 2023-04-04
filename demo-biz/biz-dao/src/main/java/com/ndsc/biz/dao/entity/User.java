package com.ndsc.biz.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsp
 * @date 2023/4/4 16:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class User extends BaseEntity{

    @TableId(type = IdType.AUTO)
    private Long userId;

    private String username;

    private String password;

    private Integer age;

}
