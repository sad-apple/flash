package com.ddd.demo.domain.biz.approve.enums;

/**
 * @author zsp
 * @date 2023/7/10 14:54
 */
public enum ApproveStatus {
    /**
     * 待提交、待审批、审核通过、审核失败
     */
    pre_submit(),
    pre_approve(),
    approve_success(),
    approve_refused()

}
