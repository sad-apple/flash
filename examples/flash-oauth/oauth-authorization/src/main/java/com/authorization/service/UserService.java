package com.authorization.service;

import com.authorization.domain.user.UserInfo;
import com.authorization.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author zsp
 * @date 2023/7/17 19:30
 */
@Service
public class UserService extends ServiceImpl<UserMapper, UserInfo> implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo one = this.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, username));
        return one;
    }

}
