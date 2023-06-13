package com.aias.ocr.moduls.direction;

import ai.djl.MalformedModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.paddlepaddle.zoo.cv.objectdetection.PpWordDetectionTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import com.aias.ocr.moduls.ZooModelProvider;
import com.aias.ocr.properties.OcrDetProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文字检测模型
 *
 * @author zsp
 * @date 2023/6/9 9:47
 */
@Slf4j
public class OcrDetZooModelProvider extends ZooModelProvider<Image, DetectedObjects> {

    private OcrDetProperties ocrDetProperties;

    public OcrDetZooModelProvider(OcrDetProperties ocrDetProperties) {
        this.ocrDetProperties = ocrDetProperties;
    }

    @Override
    public Criteria<Image, DetectedObjects> getCriteria() throws IOException {

        URI uri = getUri(ocrDetProperties.getModelUrls());

        ConcurrentHashMap<String, Object> stringObjectConcurrentHashMap = new ConcurrentHashMap<>();

        return Criteria.builder()
                       .optEngine("PaddlePaddle")
//                       .optModelName("inference")
                       .setTypes(Image.class, DetectedObjects.class)
//                       .optModelUrls("jar:///models/ch_PP-OCRv3_det_infer_onnx.zip")
//                       .optModelUrls(ocrDet.getModelUrls())
                       .optModelPath(Paths.get(uri))
                       .optTranslator(new PpWordDetectionTranslator(stringObjectConcurrentHashMap))
//                       .optTranslator(new PpWordDetectionTranslator(new HashMap<String, String>()))
                       .optProgress(new ProgressBar())
                       .build();
    }

    @Override
    public ZooModel<Image, DetectedObjects> getZooModel() throws ModelNotFoundException, MalformedModelException, IOException {
        return getCriteria().loadModel();
    }

}
