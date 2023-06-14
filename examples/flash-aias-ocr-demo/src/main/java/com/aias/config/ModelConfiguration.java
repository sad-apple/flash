package com.aias.config;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import com.aias.models.OcrModel;
import com.aias.models.ocr.MultiRecognitionModel;
import com.aias.models.ocr.RecognitionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author zsp
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
    @Value("${model.enable-multi}")
    private Boolean enbaleMulti;

    @Bean
    public OcrModel recognitionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        OcrModel ocrModel = new RecognitionModel();
        if (enbaleMulti) {
            ocrModel = new MultiRecognitionModel();
        }
        if (!StringUtils.hasLength(type) || "mobile".equalsIgnoreCase(type)) {
            ocrModel.init(mobileDet, mobileRec);
        } else if ("light".equalsIgnoreCase(type)) {
            ocrModel.init(lightDet, lightRec);
        } else if ("server".equalsIgnoreCase(type)) {
            ocrModel.init(serverDet, serverRec);
        } else {
            ocrModel.init(mobileDet, mobileRec);
        }
        return ocrModel;
    }
}