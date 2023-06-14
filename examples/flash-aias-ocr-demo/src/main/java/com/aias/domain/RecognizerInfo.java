package com.aias.domain;

import ai.djl.modality.cv.output.BoundingBox;
import lombok.Data;

/**
 * @author zsp
 * @date 2023/6/14 14:27
 */
@Data
public class RecognizerInfo {

    /**
     * ConcurrentLinkedQueue size
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

}
