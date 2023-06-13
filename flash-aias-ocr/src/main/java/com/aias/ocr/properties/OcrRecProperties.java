package com.aias.ocr.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zsp
 * @date 2023/6/9 10:08
 */
@Data
@ConfigurationProperties("ocr.rec")
public class OcrRecProperties {

    private String modelUrls = "/models/rec_crnn.zip";
}
