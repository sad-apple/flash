package com.authorization.service;

import com.authorization.domain.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author zsp
 * @date 2023/7/17 14:27
 */
@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public String login( String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 获取用户Id，作为Redis的Key，User作为Value存入Redis中
        UserInfo loginUser = (UserInfo) authenticate.getPrincipal();
        loginUser.setPassword(null);

        // TODO: 2023/7/17 返回token
        return "success";
    }

    public String logout() {
        return null;
    }

}
