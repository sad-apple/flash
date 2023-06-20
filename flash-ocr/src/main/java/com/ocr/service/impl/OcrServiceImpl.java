package com.ocr.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import com.ocr.domain.DataBean;
import com.ocr.domain.Point;
import com.ocr.models.OcrModel;
import com.ocr.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsp
 */
@Slf4j
@Service
public class OcrServiceImpl implements OcrService {
    private final OcrModel recognitionModel;

    public OcrServiceImpl(OcrModel recognitionModel) {
        this.recognitionModel = recognitionModel;
    }

    @Override
    public List<DataBean> getGeneralInfo(Image image) {
        try {
            List<DataBean> dataList = new ArrayList<>();
            DetectedObjects detectedObjects = recognitionModel.predict(image);
            List<DetectedObjects.DetectedObject> list = detectedObjects.items();
            for (DetectedObjects.DetectedObject result : list) {
                DataBean dataBean = new DataBean();
                List<Point> points = new ArrayList<>();
                String className = result.getClassName();
                dataBean.setValue(className);
                BoundingBox box = result.getBoundingBox();
                Rectangle rectangle = box.getBounds();

                Iterable<ai.djl.modality.cv.output.Point> pathIterator = rectangle.getPath();
                for (ai.djl.modality.cv.output.Point value : pathIterator) {
                    Point point = new Point();
                    point.setX((int) (value.getX() * image.getWidth()));
                    point.setY((int) (value.getY() * image.getHeight()));
                    points.add(point);
                }

                dataBean.setPoints(points);
                dataList.add(dataBean);
            }
            return dataList;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DataBean> getGeneralInfo(InputStream inputStream) throws IOException {
        Image image = ImageFactory.getInstance().fromInputStream(inputStream);
        return getGeneralInfo(image);
    }

}
