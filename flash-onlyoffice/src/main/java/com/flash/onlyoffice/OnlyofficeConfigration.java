package com.flash.onlyoffice;

import com.flash.onlyoffice.properties.DocServiceProperties;
import com.flash.onlyoffice.properties.FileStorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zsp
 * @date 2023/8/11 11:16
 */
@ComponentScan(basePackages = "com.flash.onlyoffice")
@EnableConfigurationProperties(value = {DocServiceProperties.class, FileStorageProperties.class})
public class OnlyofficeConfigration {
}

