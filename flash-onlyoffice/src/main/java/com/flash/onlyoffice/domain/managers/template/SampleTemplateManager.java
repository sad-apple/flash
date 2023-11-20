/**
 *
 * (c) Copyright Ascensio System SIA 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.flash.onlyoffice.domain.managers.template;

import com.flash.onlyoffice.domain.managers.document.DocumentManager;
import com.flash.onlyoffice.domain.models.enums.DocumentType;
import com.flash.onlyoffice.domain.models.filemodel.Template;
import com.flash.onlyoffice.domain.storage.FileStoragePathBuilder;
import com.flash.onlyoffice.domain.util.file.FileUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author zsp
 */
@Component
@Qualifier("sample")
public class SampleTemplateManager implements TemplateManager {
    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private FileStoragePathBuilder storagePathBuilder;

    @Autowired
    private FileUtility fileUtility;

    /**
     * 创建具有指定名称的模板文档
     */
    @Override
    public List<Template> createTemplates(final String fileDir) {
//        List<Template> templates = List.of(
//                new Template("", "Blank", "http://172.168.14.59:4000/local/create?fileExt=docx&sample=false"),  // create a blank template
//                new Template(getTemplateImageUrl(fileDir), "With sample content", "http://172.168.14.59:4000/local/create?fileExt=docx&sample=false")  // create a template with sample content using the template image
//        );
//
//        return templates;

        return Collections.emptyList();
    }

    /**
     * 获取指定文件的模板图像 URL
     */
    @Override
    public String getTemplateImageUrl(final String fileName) {
        // 获取文件类型
        DocumentType fileType = fileUtility.getDocumentType(fileName);
        // 获取服务器网址
        String path = storagePathBuilder.getServerUrl(true);
        if (fileType.equals(DocumentType.word)) {
            // 获取 word 文档类型的模板图像的 URL
            return path + "/css/img/file_docx.svg";
        } else if (fileType.equals(DocumentType.slide)) {
            // 获取幻灯片文档类型的模板图像的 URL
            return path + "/css/img/file_pptx.svg";
        } else if (fileType.equals(DocumentType.cell)) {
            // get URL to the template image for the cell document type
            return path + "/css/img/file_xlsx.svg";
        }
        // get URL to the template image for the default document type (word)
        return path + "/css/img/file_docx.svg";
    }
}
