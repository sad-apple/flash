package com.example.poitl;

import com.deepoove.poi.XWPFTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author zsp
 * @date 2023/8/15 18:27
 */
public class WordUtil {

    public static void main(String[] args) throws IOException {
        URL resource = ClassUtils.getDefaultClassLoader().getResource("test.docx");
        XWPFTemplate template = XWPFTemplate.compile(resource.getPath()).render(
                new HashMap<String, Object>() {{
                    put("name", "Hi, poi-tl Word模板引擎");
                    put("test", 123);
                }});
        template.writeAndClose(
                new FileOutputStream(ClassUtils.getDefaultClassLoader().getResource("").getPath() + "output.docx"));

    }

}
