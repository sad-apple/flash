package com.ddd.demo.domain.biz.demo.service;

import com.ddd.demo.domain.biz.demo.model.DemoModel;
import com.ddd.demo.domain.biz.demo.repository.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zsp
 * @date 2023/7/6 11:15
 */
@Service
public class DemoService {

    @Autowired
    private DemoRepository demoRepository;

    public DemoModel findById(String id) {
        DemoModel demoModel = demoRepository.queryById(id);
        return demoModel;
    }
}
