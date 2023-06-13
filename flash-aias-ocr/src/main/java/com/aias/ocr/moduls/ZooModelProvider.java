package com.aias.ocr.moduls;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

/**
 * zoo model provider
 * @author zsp
 * @date 2023/6/9 9:20
 */
@Slf4j
public abstract class ZooModelProvider<I, O> {

    public String getName() {
        return "zooModelProvider";
    }

    public abstract Criteria<I, O>  getCriteria() throws IOException;

    public abstract ZooModel<I, O> getZooModel() throws ModelNotFoundException, MalformedModelException, IOException;

    public URI getUri(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        log.info("文件地址：{}", classPathResource.getURI().getPath());
        return classPathResource.getURI();
    }
}
