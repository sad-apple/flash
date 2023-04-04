package com.ndsc.biz.dao.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author zsp
 * @date 2023/4/4 15:22
 */
public class InsertBatch extends AbstractMethod {

    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    protected InsertBatch(String methodName) {
        super(methodName);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String insertBatchSql = "";
        return null;
    }

}
