package com.flash.onlyoffice.domain.managers.jwt;

import com.alibaba.fastjson2.JSONObject;
import org.primeframework.jwt.domain.JWT;

import java.util.Map;

/**
 * 指定 jwt 管理器功能
 *
 * @author zsp
 */
public interface JwtManager {

    /**
     * 检查令牌是否已启用
     *
     * @return boolean
     */
    boolean tokenEnabled();

    /**
     * 创建文档令牌
     *
     * @param payloadClaims claims
     * @return token
     */
    String createToken(Map<String, Object> payloadClaims);

    /**
     * 读取令牌
     *
     * @param token toke
     * @return jwt
     */
    JWT readToken(String token);

    /**
     * 解析body
     *
     * @param payload payload
     * @param header  header
     * @return json
     */
    JSONObject parseBody(String payload, String header);

}
