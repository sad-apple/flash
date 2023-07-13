package com.ddd.demo.repository.impl;

import com.ddd.demo.domain.biz.demo.model.DemoModel;
import com.ddd.demo.domain.biz.demo.repository.DemoRepository;
import com.ddd.demo.repository.convert.DemoConverter;
import com.ddd.demo.repository.orm.DemoDO;
import com.ddd.demo.repository.repository.DemoCuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author zsp
 * @date 2023/7/6 15:11
 */
@Repository
public class DemoRepositoryImpl implements DemoRepository{

    @Autowired
    private DemoCuRepository demoCuRepository;
    @Autowired
    private DemoConverter demoConverter;

    @Override
    public DemoModel queryById(String id) {
        Optional<DemoDO> byId = demoCuRepository.findById(id);
        if (byId.isPresent()) {
            DemoDO demoDO = byId.get();
            return demoConverter.toModel(demoDO);
        }
        return null;
    }

}
