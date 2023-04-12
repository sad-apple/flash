package com.ndsc.biz.mongo.entity;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author zsp
 * @date 2023/4/11 16:37
 */
@Data
@Document()
public class User implements Serializable {

    @Id
    private String id;

    private String username;

    private Integer age;

    private String realname;

    private String address;

    private Binary avatar;
}
