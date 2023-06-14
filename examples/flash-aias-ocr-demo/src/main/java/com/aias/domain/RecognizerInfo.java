package com.aias.domain;

import ai.djl.modality.cv.output.BoundingBox;
import com.aias.models.ocr.MultiRecognitionModel;
import lombok.Data;

/**
 * @author zsp
 * @date 2023/6/14 14:27
 */
@Data
public class RecognizerInfo {

    /**
     * 防止多线程执行之后的无序
     * {@link MultiRecognitionModel.DetectedObjectWrapper#getSort()}
     */
    private int sort;

    private String txt;

    private BoundingBox box;

    private Double prob;

    public RecognizerInfo(String txt, BoundingBox box, Double prob) {
        this.txt = txt;
        this.box = box;
        this.prob = prob;
    }

    public RecognizerInfo(String txt, BoundingBox box, Double prob, int sort) {
        this.txt = txt;
        this.box = box;
        this.prob = prob;
        this.sort = sort;
    }

}
