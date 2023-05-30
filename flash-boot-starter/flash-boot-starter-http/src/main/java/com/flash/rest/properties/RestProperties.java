package com.flash.rest.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zsp
 * @date 2023/5/30 14:31
 */
@ConfigurationProperties("flash.rest")
@Data
public class RestProperties {

    private int connectTimeout = -1;

    private int readTimeout = -1;

}
