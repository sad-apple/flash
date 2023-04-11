package com.ndsc.tool.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.ndsc.tool.mybatis.method.InsertBatch;

import java.util.List;

/**
 * @author zsp
 * @date 2023/4/4 15:24
 * 方法注入器
 */
public class NdscSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertBatch("insertBatch"));

        return methodList;
    }

}
