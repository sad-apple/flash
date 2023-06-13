package com.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import com.aias.models.ocr.RecognitionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author Calvin
 * @date Oct 19, 2021
 */
@Configuration
public class ModelConfiguration {
    //OCR Model
    @Value("${model.type}")
    private String type;
    // mobile model
    @Value("${model.mobile.det}")
    private String mobileDet;
    @Value("${model.mobile.rec}")
    private String mobileRec;
    // light model
    @Value("${model.light.det}")
    private String lightDet;
    @Value("${model.light.rec}")
    private String lightRec;
    // server model
    @Value("${model.server.det}")
    private String serverDet;
    @Value("${model.server.rec}")
    private String serverRec;

    @Bean
    public RecognitionModel recognitionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        RecognitionModel recognitionModel = new RecognitionModel();
        if (!StringUtils.hasLength(type) || "mobile".equalsIgnoreCase(type)) {
            recognitionModel.init(mobileDet, mobileRec);
        } else if ("light".equalsIgnoreCase(type)) {
            recognitionModel.init(lightDet, lightRec);
        } else if ("server".equalsIgnoreCase(type)) {
            recognitionModel.init(serverDet, serverRec);
        } else {
            recognitionModel.init(mobileDet, mobileRec);
        }
        return recognitionModel;
    }
}