package com.flash.onlyoffice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zsp
 * @date 2023/8/28 16:56
 */
@Data
@ConfigurationProperties(prefix = "only.docservice")
public class DocServiceProperties {

    private String fillformsDocs;
    private String viewedDocs;
    private String editedDocs;
    private String convertDocs;
    private String timeout;

    private String secret;
    private String header;
    private String verifyPeerOff;
    private String languages;

    private Url url;

    public String getCommandUrl() {
        return url.getSite() + url.getCommand();
    }

    @Data
    public static class Url{

        private String site;
        private String converter;
        private String command;
        private String api;
        private String preloader;
    }


}
