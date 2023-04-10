package com.ndsc.biz.dao.mybatis.annotation;

import com.ndsc.biz.dao.mybatis.constant.EncryptEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zsp
 * @date 2023/4/6 16:30
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldEncrypt {
    EncryptEnum value() default EncryptEnum.AES;
}
