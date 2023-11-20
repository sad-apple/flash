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

package com.flash.onlyoffice.configurers.implementations;

import com.flash.onlyoffice.configurers.DocumentConfigurer;
import com.flash.onlyoffice.configurers.wrappers.DefaultDocumentWrapper;
import com.flash.onlyoffice.domain.managers.document.DocumentManager;
import com.flash.onlyoffice.domain.models.filemodel.Document;
import com.flash.onlyoffice.domain.models.filemodel.Permission;
import com.flash.onlyoffice.domain.util.file.FileUtility;
import com.flash.onlyoffice.domain.util.service.ServiceConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author zsp
 */
@Service
@Primary
public class DefaultDocumentConfigurer implements DocumentConfigurer<DefaultDocumentWrapper> {

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private FileUtility fileUtility;

    @Autowired
    private ServiceConverter serviceConverter;

    @Override
    public void configure(final Document document,
                          final DefaultDocumentWrapper wrapper) {
        String fileDir = wrapper.getFileDir();
        Permission permission = wrapper.getPermission();

        document.setTitle(wrapper.getTitle());
        document.setUrl(documentManager.getDownloadUrl(fileDir, true));
        document.setUrlUser(documentManager.getFileUri(fileDir, false));
        document.setDirectUrl(wrapper.getIsEnableDirectUrl() ? documentManager.getDownloadUrl(fileDir, false) : "");
        document.setFileType(fileUtility.getFileExtension(fileDir).replace(".", ""));
        document.getInfo().setFavorite(wrapper.getFavorite());

        String key = serviceConverter.generateRevisionIdByFileDir(fileDir);

        document.setKey(key);
        document.setPermissions(permission);
    }
}
