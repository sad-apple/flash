package com.ndsc.tool.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ndsc.tool.mybatis.interceptor.FieldSensitiveInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsp
 * @date 2023/4/7 16:40
 */
@Configuration
@ComponentScan(basePackageClasses = MybatisUtilConfig.class)
public class MybatisUtilConfig {

    /**
     * 自定义 SqlInjector
     * 里面包含自定义的全局方法
     */
    @Bean
    public NdscSqlInjector ndscSqlInjector() {
        return new NdscSqlInjector();
    }

    /**
     * 自动填充数据
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
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
