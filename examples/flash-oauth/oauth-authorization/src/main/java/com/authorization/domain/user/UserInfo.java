package com.authorization.domain.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.flash.common.sensitive.constant.SensitiveType;
import com.flash.mybatis.annotation.FieldSensitive;
import com.flash.mybatis.entity.BaseEntity;
import com.flash.mybatis.handlers.EncryptTypeHandler;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author zsp
 * @date 2023/7/17 19:25
 */
@Data
@TableName(value = "au_user", autoResultMap = true)
public class UserInfo extends BaseEntity<UserInfo> implements UserDetails {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String password;

    private Integer age;

    @FieldSensitive(SensitiveType.IDCARD_STRONG)
    @TableField(typeHandler = EncryptTypeHandler.class)
    private String idCard;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
