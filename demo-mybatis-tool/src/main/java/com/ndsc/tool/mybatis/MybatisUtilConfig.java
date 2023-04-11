package com.ndsc.tool.mybatis;

import com.ndsc.tool.mybatis.interceptor.FieldSensitiveInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * @author zsp
 * @date 2023/4/7 16:40
 */
public class MybatisUtilConfig {

    /**
     * 自定义 SqlInjector
     * 里面包含自定义的全局方法
     */
    @Bean
    public NdscSqlInjector ndscSqlInjector() {
        return new NdscSqlInjector();
    }

    @Bean
    public NdscMetaObjectHandler metaObjectHandler() {
        return new NdscMetaObjectHandler();
    }

    /**
     * 敏感词拦截
     * @return
     */
    @Bean
    public FieldSensitiveInterceptor fieldSensitiveInterceptor() {
        return new FieldSensitiveInterceptor();
    }

//    @Bean
//    public FieldEncryptInterceptor fieldEncryptInterceptor() {
//        return new FieldEncryptInterceptor();
//    }

}
