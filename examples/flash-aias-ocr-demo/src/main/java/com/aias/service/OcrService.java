package com.aias.service;

import ai.djl.modality.cv.Image;
import com.aias.domain.DataBean;

import java.util.List;

/**
 * @author zsp
 */
public interface OcrService {
    List<DataBean> getGeneralInfo(Image image);
}
