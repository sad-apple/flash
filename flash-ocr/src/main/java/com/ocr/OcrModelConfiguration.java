package com.ocr;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import com.ocr.models.OcrModel;
import com.ocr.models.ocr.MultiRecognitionModel;
import com.ocr.models.ocr.RecognitionModel;
import com.ocr.service.OcrService;
import com.ocr.service.impl.OcrServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author zsp
 */
@Configuration
public class OcrModelConfiguration {
    // Model
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

    @Autowired(required = false)
    private ThreadPoolTaskExecutor taskExecutor;

    @Bean
    public OcrModel recognitionModel() throws IOException, ModelNotFoundException, MalformedModelException {
        OcrModel ocrModel = new RecognitionModel();
        if (enbaleMulti) {
            if (taskExecutor == null) {
                throw new RuntimeException("no thread pool for multi recognition model !");
            }
            ocrModel = new MultiRecognitionModel(taskExecutor);
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

    @Bean
    public OcrService ocrService() throws ModelNotFoundException, MalformedModelException, IOException {
        return new OcrServiceImpl(recognitionModel());
    }
}