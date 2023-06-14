package com.aias.models.ocr;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import com.aias.domain.RecognizerInfo;
import com.aias.models.OcrModel;
import com.aias.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    private Predictor<Image, String> recognizer;
    private Predictor<Image, DetectedObjects> detector;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void init(String detUri, String recUri) throws MalformedModelException, ModelNotFoundException, IOException {
        this.detectionModel = ModelZoo.loadModel(detectCriteria(detUri));
        this.recognitionModel = ModelZoo.loadModel(recognizeCriteria(recUri));
        this.recognizer = recognitionModel.newPredictor();
        this.detector = detectionModel.newPredictor();
    }

    @Override
    public DetectedObjects predict(Image image) throws TranslateException {
        StopWatch detectWatch = new StopWatch();
        StopWatch recognizeWatch = new StopWatch();
        detectWatch.start();
        DetectedObjects detections = detector.predict(image);
        detectWatch.stop();
        log.info("检测耗时 time:{}mm", detectWatch.getTotalTimeMillis());

        List<DetectedObjects.DetectedObject> boxes = detections.items();
        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();

        recognizeWatch.start();

        ConcurrentLinkedQueue<DetectedObjects.DetectedObject> queue = new ConcurrentLinkedQueue<>(boxes);

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

        for (Future<List<RecognizerInfo>> future : futures) {
            try {
                List<RecognizerInfo> recognizerInfos = future.get();
                for (RecognizerInfo recognizerInfo : recognizerInfos) {
                    names.add(recognizerInfo.getTxt());
                    prob.add(recognizerInfo.getProb());
                    rect.add(recognizerInfo.getBox());
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }


        recognizeWatch.stop();
        log.info("recognize time: {}mm", recognizeWatch.getTotalTimeMillis());
        return new DetectedObjects(names, prob, rect);
    }





    private class InferCallable implements Callable<List<RecognizerInfo>> {
        private final Predictor<Image, String> recognizer;
        private final ConcurrentLinkedQueue<DetectedObjects.DetectedObject> queue;
        private final Image image;
        private final List<RecognizerInfo> resultList = new ArrayList<>();


        public InferCallable(ZooModel<Image, String> recognitionModel, ConcurrentLinkedQueue<DetectedObjects.DetectedObject> queue, Image image) {
            this.recognizer = recognitionModel.newPredictor();
            this.queue = queue;
            this.image = image;
        }

        @Override
        public List<RecognizerInfo> call() throws TranslateException {
            DetectedObjects.DetectedObject box = queue.poll();

            while (box != null) {
                Image subImg = getSubImage(image, box.getBoundingBox());
                if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                    subImg = rotateImg(subImg);
                }
                String txt = recognizer.predict(subImg);
                // todo 待删除
                System.out.println(txt);
                RecognizerInfo recognizerInfo = new RecognizerInfo(txt, box.getBoundingBox(), -1.0);
                resultList.add(recognizerInfo);
                box = queue.poll();

            }
            return resultList;
        }
    }
}
