package com.authorization.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author zsp
 * @date 2023/7/17 13:23
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Value("${jwt.private.key}")
    RSAPrivateKey priv;

    @Autowired
    UserDetailsService userService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        return authenticationManager;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*@Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setJdbcTemplate(jdbcTemplate);
        return jdbcTokenRepository;
    }

    @Bean
    public RememberMeServices rememberMeServices(PersistentTokenRepository persistentTokenRepository) {
        PersistentTokenBasedRememberMeServices rememberMeServices =
                new PersistentTokenBasedRememberMeServices("zsp", userService, persistentTokenRepository);
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }*/
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        http.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(withDefaults()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authenticationRequest -> authenticationRequest
//                    .requestMatchers("/auth/login", "/oauth2/**").permitAll()
//                    .requestMatchers("/**").permitAll()
.anyRequest().authenticated())
            .headers(headers -> headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable));
        http.cors(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.exceptionHandling((exceptions) -> exceptions
                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}

