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
@TableName("biz_user")
public class BizUser extends BaseEntity{

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String username;

    private String cid;

    private String phone;

}
