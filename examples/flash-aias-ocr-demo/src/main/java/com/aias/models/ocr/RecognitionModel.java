package com.aias.models.ocr;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.paddlepaddle.zoo.cv.objectdetection.PpWordDetectionTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import com.aias.models.OcrModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zsp
 */
@Slf4j
public final class RecognitionModel extends OcrModel {

    private ZooModel<Image, DetectedObjects> detectionModel;
    private ZooModel<Image, String> recognitionModel;
    private Predictor<Image, String> recognizer;
    private Predictor<Image, DetectedObjects> detector;

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
            System.out.println(name);
            names.add(name);
            prob.add(-1.0);
            rect.add(box.getBoundingBox());
        }
        recognizeWatch.stop();
        log.info("recognize time：{}mm", recognizeWatch.getTotalTimeMillis());
        return new DetectedObjects(names, prob, rect);
    }

    private Criteria<Image, DetectedObjects> detectCriteria(String detUri) throws IOException {
        URI uri = getUri(detUri);

        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get(uri))
                        .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    private Criteria<Image, String> recognizeCriteria(String recUri) throws IOException {
        URI uri = getUri(recUri);
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get(uri))
//                        .optModelPath(Paths.get("E:\\workspace\\person\\ocr\\ocr-sdk-new\\models\\rec_crnn.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator())
                        .build();

        return criteria;
    }

    private Image getSubImage(Image img, BoundingBox box) {
        Rectangle rect = box.getBounds();
        double[] extended = extendRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        int width = img.getWidth();
        int height = img.getHeight();
        int[] recovered = {
                (int) (extended[0] * width),
                (int) (extended[1] * height),
                (int) (extended[2] * width),
                (int) (extended[3] * height)
        };
        return img.getSubImage(recovered[0], recovered[1], recovered[2], recovered[3]);
    }

    private double[] extendRect(double xmin, double ymin, double width, double height) {
        double centerx = xmin + width / 2;
        double centery = ymin + height / 2;
        if (width > height) {
            width += height * 2.0;
            height *= 3.0;
        } else {
            height += width * 2.0;
            width *= 3.0;
        }
        double newX = centerx - width / 2 < 0 ? 0 : centerx - width / 2;
        double newY = centery - height / 2 < 0 ? 0 : centery - height / 2;
        double newWidth = newX + width > 1 ? 1 - newX : width;
        double newHeight = newY + height > 1 ? 1 - newY : height;
        return new double[]{newX, newY, newWidth, newHeight};
    }

    private Image rotateImg(Image image) {
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
            return ImageFactory.getInstance().fromNDArray(rotated);
        }
    }

}
