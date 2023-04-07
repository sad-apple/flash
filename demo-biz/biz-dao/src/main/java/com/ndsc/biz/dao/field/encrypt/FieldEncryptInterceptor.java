package com.ndsc.biz.dao.field.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;
import java.util.Iterator;
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
public class FieldEncryptInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        log.info("自定义拦截");
        return process(invocation);
//        Object proceed = invocation.proceed();
    }

    public static Object process(Invocation invocation) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        List proceed = (List)invocation.proceed();
        if (proceed.isEmpty()) {
            return proceed;
        } else {
            Object target = invocation.getTarget();
            if (target instanceof DefaultResultSetHandler) {
                DefaultResultSetHandler handler = (DefaultResultSetHandler) target;
                Field mappedStatement = handler.getClass().getDeclaredField("mappedStatement");
                mappedStatement.setAccessible(true);
                MappedStatement statement = (MappedStatement)mappedStatement.get(handler);
                Configuration configuration = statement.getConfiguration();

                Iterator iterator = proceed.iterator();
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    Class<?> aClass = next.getClass();
                    Field[] fields = aClass.getDeclaredFields();
                    for (Field field : fields) {
                        FieldEncrypt fieldEncrypt = field.getAnnotation(FieldEncrypt.class);
                        if (fieldEncrypt != null) {
                            log.info(field.getName());
                        }
                    }

                }
            }
        }
        return proceed;

    }


    /*public static Object test1(Invocation var0, BiConsumer<MetaObject, FieldSetProperty> var1) throws Throwable {
        List var2 = (List)var0.proceed();
        if (var2.isEmpty()) {
            return var2;
        } else {
            Object var3 = var0.getTarget();
            if (var3 instanceof DefaultResultSetHandler) {
                DefaultResultSetHandler var4 = (DefaultResultSetHandler)var3;
                Field var5 = var4.getClass().getDeclaredField("mappedStatement");
                var5.setAccessible(true);
                MappedStatement var6 = (MappedStatement)var5.get(var4);
                Configuration var7 = var6.getConfiguration();
                Iterator var8 = var2.iterator();

                while(var8.hasNext()) {
                    Object var9 = var8.next();
                    if (null != var9 && !test2(var7, var9, var1)) {
                        break;
                    }
                }
            }

            return var2;
        }
    }

    public static boolean test2(Configuration config, Object var1, BiConsumer<MetaObject, FieldSetProperty> var2) {
        List var3 = getResult(var1.getClass());
        if (!CollectionUtils.isEmpty(var3)) {
            MetaObject var4 = config.newMetaObject(var1);
            var3.parallelStream().forEach((var2x) -> {
                var2.accept(var4, var2x);
            });
            return true;
        } else {
            return false;
        }
    }

    private static Set<Class<?>> O000oOO;
    public static List<FieldSetProperty> getResult(Class<?> var0) {
        if (O000oOO.contains(var0)) {
            return null;
        } else {
            Object var1 = (List)O000oOO0.get(var0);
            if (null == var1) {
                if (var0.isAssignableFrom(HashMap.class)) {
                    O000oOO.add(var0);
                } else {
                    var1 = new ArrayList();
                    List var2 = O00oOooO.O00000o0(var0);
                    Iterator var3 = var2.iterator();

                    while(true) {
                        Field var4;
                        FieldEncrypt var5;
                        do {
                            if (!var3.hasNext()) {
                                if (((List)var1).isEmpty()) {
                                    O000oOO.add(var0);
                                } else {
                                    O000oOO0.put(var0, var1);
                                }

                                return (List)var1;
                            }

                            var4 = (Field)var3.next();
                            var5 = null;
                            if (O000oO0o) {
                                var5 = (FieldEncrypt)var4.getAnnotation(FieldEncrypt.class);
                                if (null != var5 && !var4.getType().isAssignableFrom(String.class)) {
                                    throw new O000000o("annotation `@FieldEncrypt` only string types are supported.");
                                }
                            }

                            var6 = null;
                            if (O000oO) {
                                var6 = (FieldBind)var4.getAnnotation(FieldBind.class);
                            }
                        } while(null == var5 && null == var6);

                        ((List)var1).add(new FieldSetProperty(var4.getName(), var5, var6));
                    }
                }
            }

            return (List)var1;
        }
    }*/
}
