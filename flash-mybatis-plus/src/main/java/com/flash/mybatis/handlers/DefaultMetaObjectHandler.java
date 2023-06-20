package com.flash.mybatis.handlers;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.StringUtils;

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

        Object createByObj = metaObject.getValue("createBy");
        String createBy =
                createByObj != null && StringUtils.hasLength((String) createByObj) ? (String) createByObj : "zsp";

        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now())
            .strictInsertFill(metaObject, "insertTime", LocalDateTime.class, LocalDateTime.now())
            .strictInsertFill(metaObject, "createBy", String.class, createBy);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("start update fill ....");
        // todo 自动填充修改人

        Object updateByObj = metaObject.getValue("updateBy");
        String updateBy =
                updateByObj != null && StringUtils.hasLength((String) updateByObj) ? (String) updateByObj : "zsp";
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now())
            .strictUpdateFill(metaObject, "updateBy", String.class, updateBy);
    }

}
