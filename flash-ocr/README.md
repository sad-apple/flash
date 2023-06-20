# 基于djl的ocr工具



> 本工具是一个基于深度学习ocr（Optical Character Recognition，即光学字符识别）技术实现的图片文字识别工具。
>
> ocr模型使用的是百度飞桨（[paddlepaddle](https://www.paddlepaddle.org.cn/)），开发工具库使用的是djl（[Deep Java Library](https://djl.ai/website/blog.html)），此外，本工具大部分借鉴了开源项目[AIAS](https://gitee.com/mymagicpower/AIAS)的代码。
>
> 其中djl提供开发工具库，包括基于javacv的图像处理工具、模型加载、文字检测、文字识别，paddle提供ocr推到模型。



## 使用到的技术

### AIAS

aias并不是成熟的开源项目，代码中也存在很多问题，但是代码量不多，都是基于djl工具包进行封装，适合上手学习。

- 简介：AIAS (AI Acceleration Suite) - 人工智能加速器套件。提供: 包括SDK，平台引擎，场景套件在内，合计超过100个项目组成的项目集。
- 官网地址：http://aias.top/
- gitee：https://gitee.com/mymagicpower/AIAS

### paddleOcr

paddlepaddle是百度开源的深度学习平台，可以在此平台上完成深度学习相关的模型训练，推理。因为paddle主要是基于python进行开发训练，和本项目的语言环境冲突，所以本项目采用的是djl进行开发，其中使用到的ocr推理模型使用的是paddleOcr。

- 官网地址：https://www.paddlepaddle.org.cn/
- paddleOcr：https://www.paddlepaddle.org.cn/hub/scene/ocr
- ocr模型地址：https://aistudio.baidu.com/aistudio/modelsoverview

### DJL

djl是一个开源的深度学习java开发工具包，支持主流的深度学习工具，其中包括paddle，是本项目核心开发库。

- 官网地址：https://djl.ai/website/blog.html
- paddle相关文档：https://docs.djl.ai/engines/paddlepaddle/index.html
- djl-spring-boot-starter：https://github.com/deepjavalibrary/djl-spring-boot-starter 



## 快速上手

- 通过配置文件配置模型

    ```yaml
    # Model type: mobile, light, server
    ocr:
      # 是否开启多线程模式
      enable-multi: false
      # 启用哪个模型配置
      enable-model: server
      # 配置模型
      models:
        # 模型名称
        mobile:
          # ocr检测模型（detection model）地址
          det: examples/flash-ocr-demo/models/ch_ppocr_mobile_v2.0_det_infer.zip
          # ocr识别模型（recognition model）地址
          rec: examples/flash-ocr-demo/models/ch_ppocr_mobile_v2.0_rec_infer.zip
        light:
          det: examples/flash-ocr-demo/models/ch_PP-OCRv2_det_infer.zip
          rec: examples/flash-ocr-demo/models/ch_PP-OCRv2_rec_infer.zip
        server:
          det: examples/flash-ocr-demo/models/ch_ppocr_server_v2.0_det_infer.zip
          rec: examples/flash-ocr-demo/models/ch_ppocr_server_v2.0_rec_infer.zip
    ```

- 文字识别

  ```java
      // 注入ocrService
      @Autowired
      private OcrService ocrService;
  
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
              List<DataBean> dataList = ocrService.getGeneralInfo(image);
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
  ```

> 多线程模式需要使用到spring的`ThreadPoolTaskExecutor`线程池，默认情况下springboot会在`TaskExecutionAutoConfiguration.class`中默认添加一个线程池，通过配置文件`TaskExecutionProperties.class`可以修改线程池的配置，也可以手动添加新的线程池。
