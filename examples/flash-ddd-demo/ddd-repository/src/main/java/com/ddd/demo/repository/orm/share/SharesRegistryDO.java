package com.ddd.demo.repository.orm.share;

import com.ddd.demo.repository.orm.BaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * 股份注册
 * @author zsp
 * @date 2023/7/10 14:07
 */
@Data
@Document("shares_registry")
public class SharesRegistryDO extends BaseEntity {

    private String orgId;

    private String orgName;

    private String orgCode;

    private String manageModel;

    private LocalDate reformDate;

    private Long familyNum;

    private Long memberNum;

    private Long shareAmount;

    private int status;
}
