package com.aias.ocr;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import com.aias.ocr.moduls.classification.OcrClsZooModelProvider;
import com.aias.ocr.moduls.direction.OcrDetZooModelProvider;
import com.aias.ocr.moduls.recognition.OcrRecZooModelProvider;
import com.aias.ocr.properties.OcrClsProperties;
import com.aias.ocr.properties.OcrDetProperties;
import com.aias.ocr.properties.OcrRecProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * @author zsp
 * @date 2023/6/9 10:03
 */
@Slf4j
//@Configuration
@ComponentScan
@EnableConfigurationProperties({OcrDetProperties.class, OcrRecProperties.class, OcrClsProperties.class})
public class Ocr4AiasConfiguration {

    private final OcrDetProperties ocrDet;
    private final OcrRecProperties ocrRec;
    private final OcrClsProperties ocrCls;

    public Ocr4AiasConfiguration(OcrDetProperties ocrDet, OcrRecProperties ocrRec, OcrClsProperties ocrCls) {
        this.ocrDet = ocrDet;
        this.ocrRec = ocrRec;
        this.ocrCls = ocrCls;
    }

    /**
     * 文字检测模型
     * @return
     */
    @Bean("ocrDetZooModel")
    public ZooModel<Image, DetectedObjects> ocrDetZooModel() {
        OcrDetZooModelProvider ocrDetZooModelProvider = new OcrDetZooModelProvider(ocrDet);
        ZooModel<Image, DetectedObjects> zooModel;
        try {
            zooModel = ocrDetZooModelProvider.getZooModel();
        } catch (ModelNotFoundException | MalformedModelException | IOException e) {
            log.error("ocr 初始化失败！");
            throw new RuntimeException(e);
        }
        return zooModel;
    }

    /**
     * 文字检测
     * Expected to be used with try-with-resources. The provided predictor is {@link AutoCloseable}.
     *
     * @param ocrDetZooModel injected configured model
     * @return provider of the predictor object
     */
    @Bean("ocrDetProvider")
    public Supplier<Predictor<Image, DetectedObjects>> ocrDetProvider(ZooModel<Image, DetectedObjects> ocrDetZooModel) {
        return ocrDetZooModel::newPredictor;
    }

    /**
     * 文字识别模型
     * @return
     */
    @Bean("ocrRecZooModel")
    public ZooModel<Image, String> ocrRecZooModel() {
        OcrRecZooModelProvider ocrRecZooModelProvider = new OcrRecZooModelProvider(ocrRec);
        ZooModel<Image, String> zooModel;
        try {
            zooModel = ocrRecZooModelProvider.getZooModel();
        } catch (ModelNotFoundException | MalformedModelException | IOException e) {
            log.error("ocr 初始化失败！");
            throw new RuntimeException(e);
        }
        return zooModel;
    }

    /**
     * 文字识别
     */
    @Bean("ocrRecProvider")
    public Supplier<Predictor<Image, String>> ocrRecProvider(ZooModel<Image, String> ocrRecZooModel) {
        return ocrRecZooModel::newPredictor;
    }


    /**
     * 文字识别模型
     * @return
     */
    @Bean("ocrClsZooModel")
    public ZooModel<Image, Classifications> ocrClsZooModel() {
        OcrClsZooModelProvider ocrClsZooModelProvider = new OcrClsZooModelProvider(ocrCls);
        ZooModel<Image, Classifications> zooModel;
        try {
            zooModel = ocrClsZooModelProvider.getZooModel();
        } catch (ModelNotFoundException | MalformedModelException | IOException e) {
            log.error("ocr 初始化失败！");
            throw new RuntimeException(e);
        }
        return zooModel;
    }

    /**
     * 文字识别
     */
    @Bean("ocrClsProvider")
    public Supplier<Predictor<Image, Classifications>> ocrClsProvider(ZooModel<Image, Classifications> ocrClsZooModel) {
        return ocrClsZooModel::newPredictor;
    }
}
