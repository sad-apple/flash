package com.flash.onlyoffice.domain.util;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

/**
 * poi-tl工具
 * @author zsp
 * @date 2023/8/15 18:27
 */
public class PoiTlUtil {

    public static void templateParse(URL templUrl, Map<String, Object> param, FileOutputStream outputStream) throws IOException {
        XWPFTemplate template = XWPFTemplate.compile(templUrl.getPath()).render(param);
        template.writeAndClose(outputStream);
    }

    public static void templateParse(String templUrl, Map<String, Object> param, String outputUrl) throws IOException {

        ConfigureBuilder builder = Configure.builder();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (entry.getValue() instanceof Collection<?>) {
                builder.bind(entry.getKey(), new LoopRowTableRenderPolicy());
            }
        }
        Configure config = builder.build();

        XWPFTemplate template = XWPFTemplate.compile(templUrl, config).render(param);

        Path outputPath = Paths.get(outputUrl).getParent();
        if (!Files.exists(outputPath)) {
            try {
                Files.createDirectories(outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(outputUrl);
        template.writeAndClose(fileOutputStream);
    }

}
