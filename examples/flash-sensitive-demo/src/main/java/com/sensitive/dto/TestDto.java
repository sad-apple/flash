package com.sensitive.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flash.common.sensitive.annotations.SensitiveFormat;
import com.flash.common.sensitive.constant.SensitiveType;

/**
 * @author zsp
 * @date 2023/3/31 9:02
 */
public class TestDto {

    private String email;

    @SensitiveFormat(SensitiveType.MOBILE_PHONE)
    private String phone;

    public TestDto(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(new TestDto("18654121005"));
        System.out.println(s);
    }
}
