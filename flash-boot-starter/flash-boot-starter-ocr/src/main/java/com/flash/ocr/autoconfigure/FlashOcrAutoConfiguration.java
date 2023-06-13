package com.flash.ocr.autoconfigure;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import com.aias.ocr.Ocr4AiasConfiguration;
import com.aias.ocr.apps.OcrDirectionDetector;
import com.aias.ocr.common.DirectionInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.function.Supplier;

/**
 * @author zsp
 * @date 2023/6/9 11:24
 */
@Import(Ocr4AiasConfiguration.class)
@Configuration
public class FlashOcrAutoConfiguration {

    /*@Resource
    private Supplier<Predictor<Image, DetectedObjects>> ocrClsDetProvider;
    @Resource
    private Supplier<Predictor<Image, DirectionInfo>> ocrClsProvider;

    @Bean
    public OcrDirectionDetector ocrDirectionDetector() {
        return new OcrDirectionDetector(ocrClsDetProvider, ocrClsProvider);
    }*/
}
