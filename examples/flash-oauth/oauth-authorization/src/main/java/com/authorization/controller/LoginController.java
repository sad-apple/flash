package com.authorization.controller;

import com.authorization.dto.LoginInfo;
import com.authorization.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author zsp
 * @date 2023/7/17 13:57
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    JwtEncoder encoder;

    @Autowired
    private LoginService loginService;

    @PostMapping("/jwt")
    public String jwt(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;
        // @formatter:off
        String scope = authentication.getAuthorities().stream()
                                     .map(GrantedAuthority::getAuthority)
                                     .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                                          .issuer("self")
                                          .issuedAt(now)
                                          .expiresAt(now.plusSeconds(expiry))
                                          .subject(authentication.getName())
                                          .claim("scope", scope)
                                          .build();
        // @formatter:on
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//        return loginService.login(loginInfo.getUsername(), loginInfo.getPassword());
    }

    @PostMapping("/uuid")
    public String uuid(Authentication authentication) {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

}
