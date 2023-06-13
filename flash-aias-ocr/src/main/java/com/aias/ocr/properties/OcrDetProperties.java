package com.aias.ocr.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zsp
 * @date 2023/6/9 9:59
 */
@Data
@ConfigurationProperties("ocr.det")
public class OcrDetProperties {

    private String modelUrls = "/models/det_db.zip";
}
