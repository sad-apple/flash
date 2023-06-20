package com.flash.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zsp
 * @date 2023/5/29 14:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseEntity <T extends Model<?>> extends Model<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设置逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    private int isDel = 1;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
