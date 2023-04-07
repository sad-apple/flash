package com.ndsc.biz.dao.field.encrypt;

/**
 * @author zsp
 * @date 2023/4/6 16:30
 */
public class FieldSetProperty {
    private String fieldName;
    private FieldEncrypt fieldEncrypt;

    public String getFieldName() {
        return this.fieldName;
    }

    public FieldEncrypt getFieldEncrypt() {
        return this.fieldEncrypt;
    }


    public void setFieldName(String var1) {
        this.fieldName = var1;
    }

    public void setFieldEncrypt(FieldEncrypt var1) {
        this.fieldEncrypt = var1;
    }


    public FieldSetProperty(String var1, FieldEncrypt var2) {
        this.fieldName = var1;
        this.fieldEncrypt = var2;
    }

}
