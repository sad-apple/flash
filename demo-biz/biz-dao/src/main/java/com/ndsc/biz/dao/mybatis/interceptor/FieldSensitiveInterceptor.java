package com.ndsc.biz.dao.mybatis.interceptor;

import com.ndsc.biz.dao.mybatis.annotation.FieldSensitive;
import com.ndsc.core.sensitive.SensitiveInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author zsp
 * @date 2023/4/6 15:46
 */
@Slf4j
@Intercepts(
        {
                @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
        }
)
public class FieldSensitiveInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        System.out.println("Result Plugin 拦截 :"+invocation.getMethod());
        Object result = invocation.proceed();
        if (result instanceof Collection) {
            Collection<Object> objList= (Collection) result;
            List<Object> resultList=new ArrayList<>();
            for (Object obj : objList) {
                resultList.add(sensitize(obj));
            }
            return resultList;
        }else {
            return sensitize(result);
        }
    }

    //脱敏方法
    private Object sensitize(Object object) throws InvocationTargetException, IllegalAccessException, IOException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            FieldSensitive confidential = field.getAnnotation(FieldSensitive.class);
            if (confidential==null){
                continue;
            }
            PropertyDescriptor ps = BeanUtils.getPropertyDescriptor(object.getClass(), field.getName());
            if (ps.getReadMethod() == null || ps.getWriteMethod() == null) {
                continue;
            }
            Object value = ps.getReadMethod().invoke(object);
            if (value != null) {
                System.out.println(value);
                try {
                    String serialize = SensitiveInfoUtil.sensitize(confidential.value(), value);
                    ps.getWriteMethod().invoke(object, serialize);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    throw e;
                }
            }
        }
        return object;
    }
}
