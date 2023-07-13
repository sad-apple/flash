package com.ddd.demo.domain.biz.demo.repository;

import com.ddd.demo.domain.biz.demo.model.DemoModel;

/**
 * @author zsp
 * @date 2023/7/4 11:20
 */
public interface DemoRepository {

    DemoModel queryById(String id);
}
