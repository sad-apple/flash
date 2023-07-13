package com.ddd.demo.repository.convert;

import com.ddd.demo.domain.biz.demo.model.DemoModel;
import com.ddd.demo.repository.orm.DemoDO;
import org.mapstruct.Mapper;

/**
 * @author zsp
 * @date 2023/7/6 14:37
 */
@Mapper(componentModel = "spring")
public interface DemoConverter {

    DemoModel toModel(DemoDO demoDO);

}
