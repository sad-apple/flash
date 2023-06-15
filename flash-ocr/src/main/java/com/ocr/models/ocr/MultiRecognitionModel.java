package com.ocr.models.ocr;

import ai.djl.MalformedModelException;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import com.ocr.domain.RecognizerInfo;
import com.ocr.models.OcrModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author zsp
 * @date 2023/6/14 13:50
 */
@Slf4j
public class MultiRecognitionModel extends OcrModel {

    private ZooModel<Image, DetectedObjects> detectionModel;
    private ZooModel<Image, String> recognitionModel;

    private final ThreadPoolTaskExecutor taskExecutor;

    public MultiRecognitionModel(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void init(String detUri, String recUri) throws MalformedModelException, ModelNotFoundException, IOException {
        this.detectionModel = ModelZoo.loadModel(detectCriteria(detUri));
        this.recognitionModel = ModelZoo.loadModel(recognizeCriteria(recUri));
    }

    @Override
    public DetectedObjects predict(Image image) throws TranslateException {
        StopWatch detectWatch = new StopWatch();
        StopWatch recognizeWatch = new StopWatch();
        detectWatch.start();
        DetectedObjects detections;
        try (var detector = detectionModel.newPredictor()) {
            detections = detector.predict(image);
        }
        detectWatch.stop();
        log.info("detect time:{}mm", detectWatch.getTotalTimeMillis());

        List<DetectedObjects.DetectedObject> boxes = detections.items();
        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();

        recognizeWatch.start();

        ConcurrentLinkedQueue<DetectedObjectWrapper> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < boxes.size(); i++) {
            DetectedObjectWrapper detectedObjectWrapper = new DetectedObjectWrapper(boxes.get(i), i);
            queue.add(detectedObjectWrapper);
        }

        int corePoolSize = taskExecutor.getCorePoolSize();
        List<Callable<List<RecognizerInfo>>> callables = new ArrayList<>(corePoolSize);
        for (int i = 0; i < corePoolSize; i++) {

            InferCallable callable = new InferCallable(recognitionModel, queue, image);
            callables.add(callable);
        }

        List<Future<List<RecognizerInfo>>> futures = new ArrayList<>();
        for (Callable<List<RecognizerInfo>> callable : callables) {
            Future<List<RecognizerInfo>> submit = taskExecutor.submit(callable);
            futures.add(submit);
        }

        List<RecognizerInfo> recognizerInfos = new ArrayList<>();
        for (Future<List<RecognizerInfo>> future : futures) {
            try {
                List<RecognizerInfo> subInfo = future.get();
                recognizerInfos.addAll(subInfo);
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
                throw new TranslateException("multi recognition failed!");
            }
        }
        // reverse
        recognizerInfos.sort(Comparator.comparing(RecognizerInfo::getSort).reversed());
        for (RecognizerInfo recognizerInfo : recognizerInfos) {
            // todo 待删除
            System.out.println(recognizerInfo.getTxt());
            names.add(recognizerInfo.getTxt());
            prob.add(recognizerInfo.getProb());
            rect.add(recognizerInfo.getBox());
        }


        recognizeWatch.stop();
        log.info("recognize time: {}mm", recognizeWatch.getTotalTimeMillis());
        return new DetectedObjects(names, prob, rect);
    }





    private class InferCallable implements Callable<List<RecognizerInfo>> {
        private final ZooModel<Image, String> recognitionModel;
        private final ConcurrentLinkedQueue<DetectedObjectWrapper> queue;
        private final Image image;
        private final List<RecognizerInfo> resultList = new ArrayList<>();


        public InferCallable(ZooModel<Image, String> recognitionModel, ConcurrentLinkedQueue<DetectedObjectWrapper> queue, Image image) {
            this.recognitionModel = recognitionModel;
            this.queue = queue;
            this.image = image;
        }

        @Override
        public List<RecognizerInfo> call() throws TranslateException {
            try (var recognizer = recognitionModel.newPredictor()) {
                DetectedObjectWrapper wrapper = queue.poll();
                while (wrapper != null) {
                    DetectedObjects.DetectedObject box = wrapper.getDetectedObject();
                    Integer sort = wrapper.getSort();

                    Image subImg = getSubImage(image, box.getBoundingBox());
                    if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                        subImg = rotateImg(subImg);
                    }
                    String txt = recognizer.predict(subImg);
                    RecognizerInfo recognizerInfo = new RecognizerInfo(txt, box.getBoundingBox(), -1.0, sort);
                    resultList.add(recognizerInfo);
                    wrapper = queue.poll();
                }
            }
            return resultList;
        }
    }

    @Data
    public static class DetectedObjectWrapper {

        private DetectedObjects.DetectedObject detectedObject;

        private Integer sort;

        public DetectedObjectWrapper(DetectedObjects.DetectedObject detectedObject, Integer sort) {
            this.detectedObject = detectedObject;
            this.sort = sort;
        }

    }

}
