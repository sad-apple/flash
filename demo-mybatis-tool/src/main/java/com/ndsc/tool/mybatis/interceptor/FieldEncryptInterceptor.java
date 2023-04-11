package com.ndsc.tool.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.Statement;

/**
 * @author zsp
 * @date 2023/4/6 15:46
 */
@Slf4j
@Intercepts(
        {
                @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
                @Signature(type = Executor.class,method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class,method = "query", args = {MappedStatement.class, Object.class,
                                                                           RowBounds.class, ResultHandler.class }),
                @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
        }
)
public class FieldEncryptInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("Executor Plugin field 拦截 :"+invocation.getMethod());
        Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();

        // update
        if (target instanceof Executor) {
            final Executor executor = (Executor) target;
            Object parameter = args[1];
            boolean isUpdate = args.length == 2;
            MappedStatement ms = (MappedStatement) args[0];
            if (!isUpdate && ms.getSqlCommandType() == SqlCommandType.SELECT) {
                RowBounds rowBounds = (RowBounds) args[2];
                ResultHandler resultHandler = (ResultHandler) args[3];
                BoundSql boundSql;
                if (args.length == 4) {
                    boundSql = ms.getBoundSql(parameter);
                } else {
                    // 几乎不可能走进这里面,除非使用Executor的代理对象调用query[args[6]]
                    boundSql = (BoundSql) args[5];
                }
                // beforeQuery
                log.info("..............beforeQuery");
                CacheKey cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
                return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            } else if (isUpdate) {
                // beforeUpdate
                log.info("..............beforeUpdate");
            }
        // query
        } else if (target instanceof StatementHandler) {

            // StatementHandler
            final StatementHandler sh = (StatementHandler) target;
            // 目前只有StatementHandler.getBoundSql方法args才为null
            if (null == args) {
                // beforeGetBoundSql
                log.info("..............beforeGetBoundSql");
            } else {
                Connection connections = (Connection) args[0];
                Integer transactionTimeout = (Integer) args[1];
                log.info("..............beforePrepare");

            }
        } else {
            log.info("............handleResultSets");
            ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();

            Object proceed = invocation.proceed();
            return proceed;
        }

        return invocation.proceed();



    }

}
