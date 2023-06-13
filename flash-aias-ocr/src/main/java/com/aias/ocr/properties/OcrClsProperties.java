package com.aias.ocr.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zsp
 * @date 2023/6/13 9:03
 */
@Data
@ConfigurationProperties("ocr.cls")
public class OcrClsProperties {

    private String modelUrls = "/models/cls.zip";

}
