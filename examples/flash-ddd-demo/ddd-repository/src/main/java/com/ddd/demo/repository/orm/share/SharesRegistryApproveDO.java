package com.ddd.demo.repository.orm.share;

import com.ddd.demo.repository.orm.BaseApproveEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

/**
 * 股份注册申请
 *
 * @author zsp
 * @date 2023/7/10 14:07
 */
@Data
@Document("shares_approve")
public class SharesRegistryApproveDO extends BaseApproveEntity {

    @Id
    private String id;

    private String orgId;

    private String orgName;

    private String orgCode;

    private String manageModel;

    private LocalDate reformDate;

    private Long familyNum;

    private Long memberNum;

    private AssetInfo assetInfo;

    private Long shareAmount;

    private List personShares;

    @Data
    public static class AssetInfo {

        private Long landArea;

        private Long farmerLandArea;

        private Long assetAmount;

    }

}
