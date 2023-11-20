package com.flash.onlyoffice;

import com.flash.onlyoffice.domain.storage.FileStoragePathBuilder;
import com.flash.onlyoffice.domain.util.SslUtils;
import com.flash.onlyoffice.properties.DocServiceProperties;
import com.flash.onlyoffice.properties.FileStorageProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author zsp
 * @date 2023/8/11 11:16
 */
@Configuration
@ComponentScan(basePackages = "com.flash.onlyoffice")
@EnableConfigurationProperties(value = {DocServiceProperties.class, FileStorageProperties.class})
public class OnlyofficeConfigration {
    public static final String TRUE = "true";

    @Autowired
    private FileStoragePathBuilder storagePathBuilder;
    @Autowired
    private FileStorageProperties fileStorageProperties;
    @Autowired
    private DocServiceProperties docServiceProperties;
    @Autowired
    private SslUtils ssl;



    /**
     * 初始化存储路径构建器
     */
    @PostConstruct
    public void init() {
        String path = fileStorageProperties.getPath();
        storagePathBuilder.configure(path.isBlank() ? null : path);
        String verifyPeerOff = docServiceProperties.getVerifyPeerOff();
        if (!verifyPeerOff.isEmpty()) {
            try {
                if (TRUE.equals(verifyPeerOff)) {
                    // 证书将被忽略
                    ssl.turnOffSslChecking();
                } else {
                    // 证书将被验证
                    ssl.turnOnSslChecking();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

