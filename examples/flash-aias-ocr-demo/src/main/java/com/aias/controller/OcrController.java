package com.aias.controller;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import com.aias.domain.DataBean;
import com.aias.service.OcrService;
import com.flash.web.response.Response;
import com.flash.web.response.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * ocr文字识别
 * @author zsp
 */
@RestController
@Slf4j
@RequestMapping("/ocr")
public class OcrController {


    @Autowired
    private OcrService inferService;

    /**
     * 根据url进行解析
     * @param url
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/generalInfoForImageUrl")
    public ResponseEntity<?> generalInfoForImageUrl(@RequestParam(value = "url") String url) throws IOException {
        Image image = ImageFactory.getInstance().fromUrl(url);
        List<DataBean> dataList = inferService.getGeneralInfo(image);
        return Response.success(dataList);
    }

    /**
     * 上传图片进行解析
     * @param imageFile
     * @return
     */
    @PostMapping("/generalInfoForImageFile")
    public ResponseEntity<?> generalInfoForImageFile(@RequestParam(value = "imageFile") MultipartFile imageFile) {
        InputStream inputStream = null;
        try {
            inputStream = imageFile.getInputStream();
            String base64Img = Base64.encodeBase64String(imageFile.getBytes());

            Image image = ImageFactory.getInstance().fromInputStream(inputStream);
            List<DataBean> dataList = inferService.getGeneralInfo(image);
            
            return Response.success(dataList);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return Response.failure(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
