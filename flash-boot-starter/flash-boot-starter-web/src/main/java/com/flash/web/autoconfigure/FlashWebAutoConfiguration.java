package com.flash.web.autoconfigure;

import com.flash.framework.utils.BeanContext;
import com.flash.web.handler.DefaultExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zsp
 * @date 2023/5/30 14:21
 */
@Configuration
public class FlashWebAutoConfiguration {

    @Bean
    public BeanContext beanContext() {
        return new BeanContext();
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultExceptionHandler defaultExceptionHandler() {
        return new DefaultExceptionHandler();
    }
}
