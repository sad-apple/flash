package com.aias.ocr.apps;

import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.translate.TranslateException;
import com.aias.ocr.common.ImageUtils;
import com.aias.ocr.common.RotatedBox;
import com.aias.ocr.common.RotatedBoxCompX;
import com.aias.ocr.opencv.NDArrayUtils;
import com.aias.ocr.opencv.OpenCVUtils;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author zsp
 * @date 2023/6/12 11:30
 */
@Slf4j
@Service
public class OcrRecService {

    @Autowired
    private Supplier<Predictor<Image, DetectedObjects>> ocrDetProvider;
    @Autowired
    private Supplier<Predictor<Image, String>> ocrRecProvider;
    @Autowired
    private Supplier<Predictor<Image, Classifications>> ocrClsProvider;

    public List<String> predict(Image image)
            throws TranslateException {

        try (Predictor<Image, Classifications> classifier = ocrClsProvider.get();
             Predictor<Image, DetectedObjects> detector = ocrDetProvider.get();
             Predictor<Image, String> recognizer = ocrRecProvider.get();
        ) {
            var detectedObj = detector.predict(image);
            Image newImage = image.duplicate();
            newImage.drawBoundingBoxes(detectedObj);
            newImage.getWrappedImage();
            ImageUtils.saveImage(newImage, "ocr_result.png", "build/output");
            //

            List<DetectedObjects.DetectedObject> boxes = detectedObj.items();
            int count = 0;

            List<String> names = new ArrayList<>();
            List<Double> prob = new ArrayList<>();
            List<BoundingBox> rect = new ArrayList<>();

            for (DetectedObjects.DetectedObject box : boxes) {
                var subImg = getSubImage(image, box.getBoundingBox());
                /*if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                    subImg = rotateImg(subImg);
                }

                Classifications.Classification result = classifier.predict(subImg).best();
                if ("Rotate".equals(result.getClassName()) && result.getProbability() > 0.5) {
                    subImg = rotateImg(subImg);
                }*/

                String name = recognizer.predict(subImg);

                names.add(name);
//            System.out.println(name);
                prob.add(-1.0);
                rect.add(box.getBoundingBox());


//                ImageUtils.saveImage(subImg, count + ".png", "build/output");

                count++;
            }

            Collections.reverse(names);
            String join = String.join("\n", names);
            System.out.println(join);
            return names;
//            newImage.drawBoundingBoxes(new DetectedObjects(names, prob, rect));
//            newImage.getWrappedImage();

//            ImageUtils.saveImage(newImage, "ocr_result_e.png", "build/output");
        }
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
        return new double[] {newX, newY, newWidth, newHeight};
    }

    public static Image rotateImg(Image image) {
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
            return ImageFactory.getInstance().fromNDArray(rotated);
        }
    }
}
