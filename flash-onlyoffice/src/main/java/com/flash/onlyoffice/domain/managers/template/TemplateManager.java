/**
 * (c) Copyright Ascensio System SIA 2023
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flash.onlyoffice.domain.managers.template;

import com.flash.onlyoffice.domain.models.filemodel.Template;

import java.util.List;

/**
 * 指定模板管理器功能
 * @author zsp
 */
public interface TemplateManager {

    /**
     * 创建具有指定名称的模板文档
     * @param fileDir 文件相对路径
     * @return 模板
     */
    List<Template> createTemplates(String fileDir);

    /**
     * 获取指定文件的模板图像 URL
     * @param fileName
     * @return
     */
    String getTemplateImageUrl(String fileName);

}
