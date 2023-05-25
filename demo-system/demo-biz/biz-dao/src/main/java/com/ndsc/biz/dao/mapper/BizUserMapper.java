package com.ndsc.biz.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ndsc.biz.dao.entity.BizUser;

/**
 * @author zsp
 * @date 2023/4/4 16:39
 */
public interface BizUserMapper extends BaseMapper<BizUser> {

    BizUser selectByBizId(String id);
}