package com.ndsc.biz.service.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ndsc.biz.dao.entity.BizUser;
import com.ndsc.biz.dao.mapper.BizUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zsp
 * @date 2023/4/4 16:38
 */
@Service
@Transactional
public class BizUserService extends ServiceImpl<BizUserMapper, BizUser> {

    public BizUser selectById(String id) {
        return baseMapper.selectByBizId(id);
    }

}
