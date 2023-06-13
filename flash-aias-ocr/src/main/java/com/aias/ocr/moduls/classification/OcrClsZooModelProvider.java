package com.aias.ocr.moduls.classification;

import ai.djl.MalformedModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.paddlepaddle.zoo.cv.imageclassification.PpWordRotateTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import com.aias.ocr.moduls.ZooModelProvider;
import com.aias.ocr.properties.OcrClsProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

/**
 * @author zsp
 * @date 2023/6/13 9:00
 */
@Slf4j
public class OcrClsZooModelProvider extends ZooModelProvider<Image, Classifications> {

    private OcrClsProperties cls;

    public OcrClsZooModelProvider(OcrClsProperties cls) {
        this.cls = cls;
    }

    @Override
    public Criteria<Image, Classifications> getCriteria() throws IOException {

        URI uri = getUri(cls.getModelUrls());

        return Criteria.builder()
                       .optEngine("PaddlePaddle")
                       .setTypes(Image.class, Classifications.class)
                       .optModelPath(Paths.get(uri))
                       .optTranslator(new PpWordRotateTranslator())
                       .build();
    }

    @Override
    public ZooModel<Image, Classifications> getZooModel() throws ModelNotFoundException, MalformedModelException, IOException {
        return getCriteria().loadModel();
    }

}
