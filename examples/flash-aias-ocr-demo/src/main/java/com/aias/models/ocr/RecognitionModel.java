package com.aias.models.ocr;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import com.aias.models.OcrModel;
import com.aias.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author zsp
 */
@Slf4j
public class RecognitionModel extends OcrModel {

    private ZooModel<Image, DetectedObjects> detectionModel;
    private ZooModel<Image, String> recognitionModel;
    private Predictor<Image, String> recognizer;
    private Predictor<Image, DetectedObjects> detector;

    @Override
    public void init(String detUri, String recUri) throws MalformedModelException, ModelNotFoundException, IOException {
        this.detectionModel = ModelZoo.loadModel(detectCriteria(detUri));
        this.recognitionModel = ModelZoo.loadModel(recognizeCriteria(recUri));
        this.recognizer = recognitionModel.newPredictor();
        this.detector = detectionModel.newPredictor();
    }

    public void close() {
        this.detectionModel.close();
        this.recognitionModel.close();
        this.recognizer.close();
        this.detector.close();
    }

    public String predictSingleLineText(Image image)
            throws TranslateException {
        String text = recognizer.predict(image);
        return text;
    }

    @Override
    public DetectedObjects predict(Image image) throws TranslateException {
        StopWatch detectWatch = new StopWatch();
        StopWatch recognizeWatch = new StopWatch();
        detectWatch.start();
        DetectedObjects detections = detector.predict(image);
        detectWatch.stop();
        log.info("detect time：{}mm", detectWatch.getTotalTimeMillis());

        List<DetectedObjects.DetectedObject> boxes = detections.items();
        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();

        recognizeWatch.start();
        for (DetectedObjects.DetectedObject box : boxes) {
            Image subImg = getSubImage(image, box.getBoundingBox());
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(subImg);
            }
            String name = recognizer.predict(subImg);
            // todo 待删除
            System.out.println(name);
            names.add(name);
            prob.add(-1.0);
            rect.add(box.getBoundingBox());
        }
        recognizeWatch.stop();
        log.info("recognize time：{}mm", recognizeWatch.getTotalTimeMillis());
        return new DetectedObjects(names, prob, rect);
    }

}
