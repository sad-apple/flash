package com.aias.service.impl;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import com.aias.domain.DataBean;
import com.aias.domain.Point;
import com.aias.models.ocr.RecognitionModel;
import com.aias.service.OcrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Calvin
 * @date Jun 12, 2021
 */
@Service
public class OcrServiceImpl implements OcrService {
    private Logger logger = LoggerFactory.getLogger(OcrServiceImpl.class);
    @Autowired
    private RecognitionModel recognitionModel;

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
            logger.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
