package com.ndsc.biz.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ndsc.biz.dao.mybatis.annotation.FieldSensitive;
import com.ndsc.biz.dao.mybatis.type.EncryptTypeHandler;
import com.ndsc.core.sensitive.SensitiveType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsp
 * @date 2023/4/4 16:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(autoResultMap = true)
public class BizUser extends BaseEntity{

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String username;

    @FieldSensitive(SensitiveType.IDCARD_STRONG)
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String cid;

    @FieldSensitive(SensitiveType.MOBILE_PHONE)
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String phone;



}
