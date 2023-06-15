package com.ocr.models;

import ai.djl.MalformedModelException;
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
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import com.ocr.models.ocr.PpWordRecognitionTranslator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zsp
 * @date 2023/6/13 15:54
 */
@Slf4j
public abstract class OcrModel {


    public URI getUri(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        log.info("file-path：{}", classPathResource.getURI().getPath());
        return classPathResource.getURI();
    }

    public abstract DetectedObjects predict(Image image) throws TranslateException;

    public abstract void init(String detUri, String recUri) throws MalformedModelException, ModelNotFoundException, IOException;

    protected Image getSubImage(Image img, BoundingBox box) {
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


    protected Criteria<Image, DetectedObjects> detectCriteria(String detUri) throws IOException {
//        URI uri = getUri(detUri);
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, DetectedObjects.class)
                        .optModelPath(Paths.get(detUri))
                        .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
                        .optProgress(new ProgressBar())
                        .build();

        return criteria;
    }

    protected Criteria<Image, String> recognizeCriteria(String recUri) throws IOException {
//        URI uri = getUri(recUri);
        Criteria<Image, String> criteria =
                Criteria.builder()
                        .optEngine("PaddlePaddle")
                        .setTypes(Image.class, String.class)
                        .optModelPath(Paths.get(recUri))
//                        .optModelPath(Paths.get("E:\\workspace\\person\\ocr\\ocr-sdk-new\\models\\rec_crnn.zip"))
                        .optProgress(new ProgressBar())
                        .optTranslator(new PpWordRecognitionTranslator())
                        .build();

        return criteria;
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

    protected Image rotateImg(Image image) {
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
            return ImageFactory.getInstance().fromNDArray(rotated);
        }
    }
}
