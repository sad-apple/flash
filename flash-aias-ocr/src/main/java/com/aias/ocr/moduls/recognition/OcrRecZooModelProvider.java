package com.aias.ocr.moduls.recognition;

import ai.djl.MalformedModelException;
import ai.djl.modality.cv.Image;
import ai.djl.paddlepaddle.zoo.cv.wordrecognition.PpWordRecognitionTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import com.aias.ocr.moduls.ZooModelProvider;
import com.aias.ocr.properties.OcrRecProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文字识别模型
 * @author zsp
 * @date 2023/6/9 9:53
 */
@Slf4j
public class OcrRecZooModelProvider extends ZooModelProvider<Image, String> {

    private OcrRecProperties ocrRecProperties;

    public OcrRecZooModelProvider(OcrRecProperties ocrRecProperties) {
        this.ocrRecProperties = ocrRecProperties;
    }


    @Override
    public Criteria<Image, String> getCriteria() throws IOException {
        ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<>();

        URI uri = getUri(ocrRecProperties.getModelUrls());
        return Criteria.builder()
                       .optEngine("PaddlePaddle")
//                       .optModelName("inference")
                       .setTypes(Image.class, String.class)
//                       .optModelUrls("jar:///models/ch_PP-OCRv3_rec_infer_onnx.zip")
//                       .optModelUrls(ocrRec.getModelUrls())
                       .optModelPath(Paths.get(uri))
                       .optProgress(new ProgressBar())
                       .optTranslator(new PpWordRecognitionTranslator())
                       .build();
    }

    @Override
    public ZooModel<Image, String> getZooModel() throws ModelNotFoundException, MalformedModelException, IOException {
        return getCriteria().loadModel();
    }

}
