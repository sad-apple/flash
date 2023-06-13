package com.aias.ocr.apps;

import ai.djl.modality.cv.Image;

/**
 * 检测器
 * @author zsp
 * @date 2023/6/8 15:38
 */
public interface Detector {

    void detect(Image image);
}
