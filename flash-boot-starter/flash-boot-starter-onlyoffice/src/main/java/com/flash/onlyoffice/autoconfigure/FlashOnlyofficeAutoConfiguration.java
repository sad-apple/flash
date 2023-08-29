package com.flash.onlyoffice.autoconfigure;

import com.flash.onlyoffice.OnlyofficeConfigration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zsp
 * @date 2023/8/28 18:34
 */
@Configuration
@Import(OnlyofficeConfigration.class)
public class FlashOnlyofficeAutoConfiguration {

}
