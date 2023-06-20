package com.ocr.service;

import ai.djl.modality.cv.Image;
import com.ocr.domain.DataBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author zsp
 */
public interface OcrService {

    List<DataBean> getGeneralInfo(Image image);

    List<DataBean> getGeneralInfo(InputStream inputStream) throws IOException;
}
