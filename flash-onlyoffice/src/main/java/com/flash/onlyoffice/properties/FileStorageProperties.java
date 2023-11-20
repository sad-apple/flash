package com.flash.onlyoffice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zsp
 * @date 2023/8/28 17:06
 */
@Data
@ConfigurationProperties(prefix = "only.fileservice")
public class FileStorageProperties {
    private String index;
    private String path;
    private String folder;
    private String filesizeMax;
    private String innerSite;
    private String outSite;
    private String callbackUrl;
    private String downloadUrl;
    private String historyPostfix;
}
