package com.aias.ocr.apps;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import com.aias.ocr.common.DirectionInfo;
import com.aias.ocr.common.ImageUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 文字方向检测器
 *
 * @author zsp
 * @date 2023/6/8 15:37
 */
@Slf4j
//@Service
public class OcrDirectionDetector implements Detector {

    Supplier<Predictor<Image, DetectedObjects>> ocrClsDetProvider;
    Supplier<Predictor<Image, DirectionInfo>> ocrClsProvider;

    public OcrDirectionDetector(Supplier<Predictor<Image, DetectedObjects>> ocrDetectorSupplier,
                                Supplier<Predictor<Image, DirectionInfo>> ocrDirectionDetectorSupplier) {
        this.ocrClsDetProvider = ocrDetectorSupplier;
        this.ocrClsProvider = ocrDirectionDetectorSupplier;
    }

    @Override
    public void detect(Image image) {
        try (var ocrDetector = ocrClsDetProvider.get();
             var ocrDirectionDetector = ocrClsProvider.get()) {
            DetectedObjects detections = predict(image, ocrDetector, ocrDirectionDetector);

            List<DetectedObjects.DetectedObject> boxes = detections.items();
            for (DetectedObjects.DetectedObject result : boxes) {
                System.out.println(result.getClassName() + " : " + result.getProbability());
            }

            ImageUtils.saveBoundingBoxImage(image, detections, "cls_detect_result.png", "build/output");
            log.info("{}", detections);
        } catch (TranslateException | IOException e) {
            e.printStackTrace();
        }
    }

    public DetectedObjects predict(Image image,
                                   Predictor<Image, DetectedObjects> detector,
                                   Predictor<Image, DirectionInfo> rotateClassifier)
            throws TranslateException {
        DetectedObjects detections = detector.predict(image);

        List<DetectedObjects.DetectedObject> boxes = detections.items();

        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();

        for (DetectedObjects.DetectedObject box : boxes) {
            Image subImg = getSubImage(image, box.getBoundingBox());
            DirectionInfo result = null;
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(subImg);
                result = rotateClassifier.predict(subImg);
                prob.add(result.getProb());
                if (result.getName().equalsIgnoreCase("Rotate")) {
                    names.add("90");
                } else {
                    names.add("270");
                }
            } else {
                result = rotateClassifier.predict(subImg);
                prob.add(result.getProb());
                if (result.getName().equalsIgnoreCase("No Rotate")) {
                    names.add("0");
                } else {
                    names.add("180");
                }
            }
            rect.add(box.getBoundingBox());
        }

        return new DetectedObjects(names, prob, rect);
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

    private Image rotateImg(Image image) {
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
            return ImageFactory.getInstance().fromNDArray(rotated);
        }
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

}
