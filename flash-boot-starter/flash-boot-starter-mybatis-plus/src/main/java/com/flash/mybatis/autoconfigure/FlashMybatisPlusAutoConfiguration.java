package com.flash.mybatis.autoconfigure;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.flash.mybatis.handlers.DefaultMetaObjectHandler;
import com.flash.mybatis.interceptor.FieldSensitiveInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis自动配置
 * @author zsp
 * @date 2023/5/29 11:30
 */
@Configuration
//@ComponentScan(basePackageClasses = FlashMybatisPlusAutoConfiguration.class)
public class FlashMybatisPlusAutoConfiguration {
    /**
     * 自动填充数据
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public DefaultMetaObjectHandler metaObjectHandler() {
        return new DefaultMetaObjectHandler();
    }

    /**
     * 敏感词拦截
     * @return
     */
    @Bean
    public FieldSensitiveInterceptor fieldSensitiveInterceptor() {
        return new FieldSensitiveInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁拦截器
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
