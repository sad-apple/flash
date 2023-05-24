package com.ndsc.tool.mybatis.annotation;

import com.ndsc.tool.mybatis.constant.SensitiveType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zsp
 * @date 2023/4/7 11:18
 * 敏感词过滤
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldSensitive {

    /**
     * 指定敏感词类型
     * @return
     */
    SensitiveType value() default SensitiveType.COMMON;

}
