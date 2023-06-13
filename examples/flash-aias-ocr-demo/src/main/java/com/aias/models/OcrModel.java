package com.aias.models;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.translate.TranslateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URI;

/**
 * @author zsp
 * @date 2023/6/13 15:54
 */
@Slf4j
public abstract class OcrModel {


    public URI getUri(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        log.info("文件地址：{}", classPathResource.getURI().getPath());
        return classPathResource.getURI();
    }

    public abstract DetectedObjects predict(Image image) throws TranslateException;
}
