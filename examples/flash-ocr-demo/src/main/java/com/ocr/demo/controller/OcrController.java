package com.ocr.demo.controller;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import com.aias.ocr.apps.OcrDirectionDetector;
import com.aias.ocr.apps.OcrRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author zsp
 * @date 2023/6/9 13:28
 */
@RestController
public class OcrController {

//    @Autowired
//    private OcrDirectionDetector detector;
    @Autowired
    private OcrRecService ocrRecService;


    @PostMapping("/ocr-direction")
    public List<String> ocrDirection() throws IOException, TranslateException {
//        Path imageFile = Paths.get("src/test/resources/ticket_90.png");
        Path imageFile = Paths.get("E:\\workspace\\person\\flash\\examples\\flash-ocr-demo\\src\\test\\resources\\ticket_0.png");
        Image image = ImageFactory.getInstance().fromFile(imageFile);
        List<String> predict = ocrRecService.predict(image);
        return predict;
    }

}
