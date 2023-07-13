package com.ddd.demo.repository.repository;

import com.ddd.demo.repository.orm.DemoDO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author zsp
 * @date 2023/7/6 16:17
 */
public interface DemoCuRepository extends MongoRepository<DemoDO, String> {

}
