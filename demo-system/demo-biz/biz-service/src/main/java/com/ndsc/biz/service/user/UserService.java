package com.ndsc.biz.service.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ndsc.biz.dao.entity.SysUser;
import com.ndsc.biz.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author zsp
 * @date 2023/4/4 16:38
 */
@Service
public class UserService extends ServiceImpl<UserMapper, SysUser> {

}
