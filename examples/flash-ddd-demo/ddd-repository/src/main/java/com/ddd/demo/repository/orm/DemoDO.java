package com.ddd.demo.repository.orm;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author zsp
 * @date 2023/7/4 11:27
 */
@Document("demo_do")
@Data
public class DemoDO {

    @Id
    private String id;

    private String name;

    private Long age;

    private String address;
}
