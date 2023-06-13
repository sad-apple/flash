package com.aias.service;

import ai.djl.modality.cv.Image;
import com.aias.domain.DataBean;

import java.util.List;

/**
 * @author Calvin
 * @date Jun 12, 2021
 */
public interface OcrService {
    List<DataBean> getGeneralInfo(Image image);
}
