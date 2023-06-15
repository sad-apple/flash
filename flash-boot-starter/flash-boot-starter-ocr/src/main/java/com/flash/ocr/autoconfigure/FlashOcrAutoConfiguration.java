package com.flash.ocr.autoconfigure;

import com.ocr.OcrModelConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zsp
 * @date 2023/6/9 11:24
 */
@Import(OcrModelConfiguration.class)
@Configuration
public class FlashOcrAutoConfiguration {

}
