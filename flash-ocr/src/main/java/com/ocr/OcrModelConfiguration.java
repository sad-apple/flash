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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author zsp
 */
@Configuration
@EnableConfigurationProperties(ModelsProperties.class)
public class OcrModelConfiguration {

    // Model
//    @Value("${ocr.enable-model}")
//    private String type;

//    @Value("${ocr.models}")
//    private Map<String, Map<String, String>> models;

    // mobile model
//    @Value("${ocr.models.mobile.det}")
//    private String mobileDet;
//    @Value("${ocr.models.mobile.rec}")
//    private String mobileRec;
//    // light model
//    @Value("${ocr.models.light.det}")
//    private String lightDet;
//    @Value("${ocr.models.light.rec}")
//    private String lightRec;
//    // server model
//    @Value("${ocr.models.server.det}")
//    private String serverDet;
//    @Value("${ocr.models.server.rec}")
//    private String serverRec;
//
//    @Value("${ocr.enable-multi}")
//    private Boolean enbaleMulti;

    @Autowired(required = false)
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private ModelsProperties modelsProperties;

    @Bean
    public OcrModel recognitionModel() throws IOException, ModelNotFoundException, MalformedModelException {

        Boolean enableMulti = modelsProperties.getEnableMulti();
        String enableModel = modelsProperties.getEnableModel();
        Map<String, ModelsProperties.Model> models = modelsProperties.getModels();

        System.out.println(modelsProperties);
        OcrModel ocrModel = new RecognitionModel();
        if (enableMulti) {
            if (taskExecutor == null) {
                throw new RuntimeException("no thread pool for multi recognition model !");
            }
            ocrModel = new MultiRecognitionModel(taskExecutor);
        }

        for (Map.Entry<String, ModelsProperties.Model> entry : models.entrySet()) {
            String type = entry.getKey();
            if (type.equals(enableModel)) {
                ModelsProperties.Model model = entry.getValue();
                ocrModel.init(model.getDet(), model.getRec());
            }
        }
        return ocrModel;
    }

    @Bean
    public OcrService ocrService() throws ModelNotFoundException, MalformedModelException, IOException {
        return new OcrServiceImpl(recognitionModel());
    }

}