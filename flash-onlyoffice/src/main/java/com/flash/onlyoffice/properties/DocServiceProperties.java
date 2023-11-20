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

    /**
     * 填充表单的文件格式
     */
    private String fillformsDocs;
    /**
     * 查看文件格式
     */
    private String viewedDocs;
    /**
     * 编辑文件格式
     */
    private String editedDocs;
    /**
     * 转换文件格式
     */
    private String convertDocs;
    /**
     * 转换超时时间
     */
    private String timeout;
    /**
     * 密钥
     */
    private String secret;
    /**
     * token header前缀
     */
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

        public String getAllApi() {
            return site + api;
        }
    }


}
