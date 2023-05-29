package com.flash.common.utils;

import com.flash.common.constant.SensitiveType;
import com.njusc.util.RegUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;

/**
 * @author zsp
 * @date 2023/3/30 19:57
 * 敏感信息处理工具类
 */
public class SensitiveInfoUtil {

    public static String sensitize(SensitiveType sensitiveType, Object value) throws IOException {
        return sensitize(sensitiveType, String.valueOf(value));
    }

    public static String sensitize(SensitiveType sensitiveType, String value) throws IOException {
        return switch (sensitiveType) {
            case COMMON -> value;
            case IDCARD_STRONG -> SensitiveInfoUtil.idCardNumStrong(value);
            case IDCARD_WEAK -> SensitiveInfoUtil.idCardNumWeak(value);
            case MOBILE_PHONE -> SensitiveInfoUtil.mobilePhone(value);
            case EMAIL -> SensitiveInfoUtil.email(value);
            case NAME -> SensitiveInfoUtil.name(value);
            default -> throw new IllegalArgumentException("unknown sensitive type enum " + sensitiveType);
        };
    }


    /**
     * [手机号码] 前三位，后四位，其他隐藏
     *
     * @param num 手机号码
     * @return <例子:138******1234>
     */
    public static String mobilePhone(String num) {
        if (!StringUtils.hasLength(num) || !RegUtil.mobile(num)) {
            return num;
        }
        int len = num.length();
        if (len < 11) {
            return num;
        }
        return formatBetween(num, 3, 4);
    }

    /**
     * [固定号码] 前4位，后4位，其他隐藏
     *
     * @param num 手机号码
     * @return <例子:0574****8866>
     */
    public static String fixedPhone(String num) {
        if (!StringUtils.hasLength(num) || !RegUtil.tel(num)) {
            return num;
        }
        return formatBetween(num, 4, 4);
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号
     *
     * @param cardNum 银行卡号
     * @return <例子:6222600**********1234>
     */
    private static String bankCard(String cardNum) {
        if (!StringUtils.hasLength(cardNum)) {
            return cardNum;
        }
        return formatBetween(cardNum, 6, 4);
    }

    /**
     * strong
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。
     *
     * @param id 身份证号
     * @return <例子：3***************2>
     */
    public static String idCardNumStrong(String id) {
        if (!StringUtils.hasLength(id) || !RegUtil.idCard(id)) {
            return id;
        }
        return formatBetween(id, 1, 1);
    }

    /**
     * weak
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。
     *
     * @param id 身份证号
     * @return <例子：3***23197402*****2>
     */
    public static String idCardNumWeak(String id) {
        if (!StringUtils.hasLength(id) || !RegUtil.idCard(id)) {
            return id;
        }
        int[] a = {1, 4};
        int[] b = {12, 17};
        return hideByIndex(id, a, b);
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示
     *
     * @param email 电子邮箱
     * @return <例子:gee**@163.com>
     */
    public static String email(String email) {
        if (!StringUtils.hasLength(email) || !RegUtil.email(email)) {
            return email;
        }
        int index = email.indexOf("@");
        if (index <= 1) {
            return email;
        }
        String begin = email.substring(0, 3);
        String end = email.substring(index);
        String stars = "***";
        return begin + stars + end;
    }

    /**
     * [中文姓名] 隐藏第一个字符为1个星号
     *
     * @param fullName 全名
     * @return <例子：*自成>
     */
    public static String name(String fullName) {
        if (!StringUtils.hasLength(fullName)) {
            return fullName;
        }
        return formatLeft(fullName, fullName.length() - 1);
    }

    /**
     * 将中间的格式化成*
     *
     * @param str      字符串
     * @param beginLen 开始保留长度
     * @param endLen   结尾保留长度
     * @return 格式化后的字符串
     */
    private static String formatBetween(String str, int beginLen, int endLen) {
        int len = str.length();
        String begin = str.substring(0, beginLen);
        String end = str.substring(len - endLen);
        String stars = String.join("", Collections.nCopies(len - beginLen - endLen, "*"));
        return begin + stars + end;
    }

    private static String hideByIndex(String str, int[]... beginLen) {
        StringBuilder builder = new StringBuilder(str);
        for (int[] ints : beginLen) {
            String stars = String.join("", Collections.nCopies(ints[1] - ints[0], "*"));
            builder.replace(ints[0], ints[1], stars);
        }
        return builder.toString();
    }

    /**
     * 将右边的格式化成*
     *
     * @param str            字符串
     * @param reservedLength 保留长度
     * @return 格式化后的字符串
     */
    private static String formatRight(String str, int reservedLength) {
        String name = str.substring(0, reservedLength);
        String stars = String.join("", Collections.nCopies(str.length() - reservedLength, "*"));
        return name + stars;
    }

    /**
     * 将左边的格式化成*
     *
     * @param str            字符串
     * @param reservedLength 保留长度
     * @return 格式化后的字符串
     */
    private static String formatLeft(String str, int reservedLength) {
        int len = str.length();
        String show = str.substring(len - reservedLength);
        String stars = String.join("", Collections.nCopies(len - reservedLength, "*"));
        return stars + show;
    }

}
