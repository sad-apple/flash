package com.flash.mybatis.handlers;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author zsp
 * @date 2023/5/29 13:18
 */
@Slf4j
public class DefaultMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("start insert fill ....");
        // todo 自动填充创建人
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now())
            .strictInsertFill(metaObject, "insertTime", LocalDateTime.class, LocalDateTime.now())
            .strictInsertFill(metaObject, "createBy", String.class, "zsp");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("start update fill ....");
        // todo 自动填充修改人
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now())
            .strictUpdateFill(metaObject, "updateBy", String.class, "zsp");
    }

}