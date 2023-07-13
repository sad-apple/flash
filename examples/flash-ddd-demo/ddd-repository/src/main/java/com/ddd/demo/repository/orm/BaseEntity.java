package com.ddd.demo.repository.orm;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @author zsp
 * @date 2023/7/10 14:16
 */
@Data
public class BaseEntity {

    @Id
    private String id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
