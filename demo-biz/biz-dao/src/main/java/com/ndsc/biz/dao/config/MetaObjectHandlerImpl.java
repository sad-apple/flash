package com.ndsc.biz.dao.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author zsp
 * @date 2023/4/4 16:10
 * 新增自动填充
 */
@Slf4j
@Component
public class MetaObjectHandlerImpl implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        //避免使用metaObject.setValue()
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now())
            .strictInsertFill(metaObject, "createBy", String.class, "zsp");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now())
            .strictUpdateFill(metaObject, "updateBy", String.class, "zsp");
    }

}
