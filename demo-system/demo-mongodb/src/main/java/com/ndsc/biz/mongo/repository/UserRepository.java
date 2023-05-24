package com.ndsc.biz.mongo.repository;

import com.ndsc.biz.mongo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author zsp
 * @date 2023/4/12 11:17
 */
public interface UserRepository extends MongoRepository<User, String> {

}
