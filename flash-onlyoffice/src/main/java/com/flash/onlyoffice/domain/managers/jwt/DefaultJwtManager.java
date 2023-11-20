/**
 * (c) Copyright Ascensio System SIA 2023
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flash.onlyoffice.domain.managers.jwt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flash.onlyoffice.properties.DocServiceProperties;
import lombok.extern.slf4j.Slf4j;
import org.primeframework.jwt.Signer;
import org.primeframework.jwt.Verifier;
import org.primeframework.jwt.domain.JWT;
import org.primeframework.jwt.hmac.HMACSigner;
import org.primeframework.jwt.hmac.HMACVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zsp
 */
@Slf4j
@Component
public class DefaultJwtManager implements JwtManager {
    public static final String PAY_LOAD= "payload";

    private final String tokenSecret;
    @Autowired
    private ObjectMapper objectMapper;

    public DefaultJwtManager(DocServiceProperties docServiceProperties) {
        this.tokenSecret = docServiceProperties.getSecret();
    }

    /**
     * create document token
     * @param payloadClaims claims
     * @return token
     */
    @Override
    public String createToken(final Map<String, Object> payloadClaims) {
        try {
            // 使用 SHA-256 哈希构建 HMAC 签名
            Signer signer = HMACSigner.newSHA256Signer(tokenSecret);
            JWT jwt = new JWT();
            for (String key : payloadClaims.keySet()) {
                jwt.addClaim(key, payloadClaims.get(key));
            }
            return JWT.getEncoder().encode(jwt, signer);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean tokenEnabled() {
        return tokenSecret != null && !tokenSecret.isEmpty();
    }

    @Override
    public JWT readToken(final String token) {
        try {
            log.info("token={}, secret={}", token, tokenSecret);
            // build a HMAC verifier using the token secret
            Verifier verifier = HMACVerifier.newVerifier(tokenSecret);

            // verify and decode the encoded string JWT to a rich object
            return JWT.getDecoder().decode(token, verifier);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public JSONObject parseBody(final String payload, final String header) {
        JSONObject body;
        try {
            body = JSON.parseObject(payload);
        } catch (Exception ex) {
            throw new RuntimeException("{\"error\":1,\"message\":\"JSON Parsing error\"}");
        }
        // 检查令牌是否已启用
        if (tokenEnabled()) {
            // 从 body 获取 token
            String token = (String) body.get("token");
            if (token == null) {
                // 并定义了标头
                if (header != null && !header.isBlank()) {

                    // 从标头获取令牌（如果存在，则将其放置在 Bearer 前缀之后）
                    token = header.startsWith("Bearer ") ? header.substring("Bearer ".length()) : header;
                }
            }
            if (token == null || token.isBlank()) {
                throw new RuntimeException("{\"error\":1,\"message\":\"JWT expected\"}");
            }

            JWT jwt = readToken(token);
            if (jwt == null) {
                throw new RuntimeException("{\"error\":1,\"message\":\"JWT validation failed\"}");
            }
            if (jwt.getObject(PAY_LOAD) != null) {
                try {
                    @SuppressWarnings("unchecked") LinkedHashMap<String, Object> jwtPayload =
                            (LinkedHashMap<String, Object>) jwt.getObject("payload");

                    jwt.claims = jwtPayload;
                } catch (Exception ex) {
                    throw new RuntimeException("{\"error\":1,\"message\":\"Wrong payload\"}");
                }
            }
            try {
                body = JSON.parseObject(objectMapper.writeValueAsString(jwt.claims));
            } catch (Exception ex) {
                throw new RuntimeException("{\"error\":1,\"message\":\"Parsing error\"}");
            }
        }

        return body;
    }

}
