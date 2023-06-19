package com.ocr;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author zsp
 * @date 2023/6/19 11:27
 */
@Data
@ConfigurationProperties(prefix = "ocr")
public class ModelsProperties {

    private Boolean enableMulti;
    private String enableModel;
    private Map<String, Model> models;

    @Data
    public static class Model{

        private String det;
        private String rec;
    }
}
