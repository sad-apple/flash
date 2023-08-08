package com.authorization.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.PasswordManagementConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author zsp
 * @date 2023/7/17 19:38
 */
@Configuration
public class AuthConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http, RegisteredClientRepository registeredClientRepository,
            AuthorizationServerSettings authorizationServerSettings) throws Exception {


        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.sessionManagement(AbstractHttpConfigurer::disable);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(withDefaults());
//        http.oauth2Client()


        /*http.authorizeHttpRequests(authenticationRequest -> authenticationRequest
                .requestMatchers("/auth/login", "/oauth2/**").permitAll()
//                    .requestMatchers("/**").permitAll()
                .anyRequest().authenticated());*/

        http.exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/login"), createRequestMatcher()));

        return http.build();
    }

    /**
     * oauth2 用于第三方认证，RegisteredClientRepository 主要用于管理第三方（每个第三方就是一个客户端）
     *
     * @return
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        JdbcRegisteredClientRepository repository =
                new JdbcRegisteredClientRepository(jdbcTemplate);

        /*RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                                                      .clientId("oidc-client")
                                                      .clientSecret("{noop}secret")
                                                      .clientAuthenticationMethod(
                                                              ClientAuthenticationMethod.CLIENT_SECRET_POST)
                                                      .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                                      .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                                                      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                                                      .redirectUri("http://www.baidu.com")
                                                      .redirectUri(
                                                              "http://localhost:8270/login/oauth2/code/oidc-client")
                                                      .redirectUri("http://localhost:8270/api/login/welcome")
                                                      .postLogoutRedirectUri("http://127.0.0.1:8270/")
                                                      .scope(OidcScopes.OPENID)
                                                      .scope(OidcScopes.PROFILE)
                                                      .scope("message.read")
                                                      .scope("message.write")
                                                      .scope("all")
                                                      // 设置 Client 需要页面审核授权
                                                      .clientSettings(
                                                              ClientSettings.builder().requireAuthorizationConsent(true)
                                                                            .build())
                                                      .build();
        repository.save(oidcClient);*/
        return repository;
    }

    /*@Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                                                      .clientId("oidc-client")
                                                      .clientSecret("{noop}secret")
                                                      .clientAuthenticationMethod(
                                                              ClientAuthenticationMethod.CLIENT_SECRET_POST)
                                                      .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                                      .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                                                      .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                                                      .redirectUri("http://www.baidu.com")
                                                      .redirectUri(
                                                              "http://localhost:8270/login/oauth2/code/oidc-client")
                                                      .redirectUri("http://localhost:8270/api/login/welcome")
                                                      .postLogoutRedirectUri("http://127.0.0.1:8270/")
                                                      .scope(OidcScopes.OPENID)
                                                      .scope(OidcScopes.PROFILE)
                                                      .scope("message.read")
                                                      .scope("message.write")
                                                      .scope("all")
                                                      // 设置 Client 需要页面审核授权
                                                      .clientSettings(
                                                              ClientSettings.builder().requireAuthorizationConsent(true)
                                                                            .build())
                                                      .build();

        return new InMemoryRegisteredClientRepository(oidcClient);
    }*/

    /**
     * 用于给access_token签名使用。
     *
     * @return
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 生成秘钥对，为jwkSource提供服务。
     *
     * @return
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    private static RequestMatcher createRequestMatcher() {
        MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
        requestMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
        return requestMatcher;
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public JdbcOAuth2AuthorizationService jdbcOAuth2AuthorizationService(RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

}

