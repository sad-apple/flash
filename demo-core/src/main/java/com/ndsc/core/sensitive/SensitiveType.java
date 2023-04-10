package com.ndsc.core.sensitive;

/**
 * @author zsp
 * @date 2023/3/31 10:13
 */
public enum SensitiveType {
    // 通用 不处理
    COMMON,
    // 姓名  <例子：*自成>
    NAME,
    // 身份证 强处理 <例子：3***************2>
    IDCARD_STRONG,
    // 身份证 弱处理 <例子：3***23197402*****2>
    IDCARD_WEAK,
    // 移动电话 <例子:138******1234>
    MOBILE_PHONE,
    // 固定电话 <例子:0574****8866>
    FIXED_PHONE,
    // 邮箱 <例子:gee**@163.com>
    EMAIL,

}
