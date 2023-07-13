package com.ddd.demo.repository.orm;

import com.ddd.demo.domain.biz.approve.enums.ApproveStatus;

/**
 * @author zsp
 * @date 2023/7/10 14:50
 */
public class BaseApproveEntity {

    private String applyBy;

    private String approveContent;

    private ApproveStatus approveStatus;

}
