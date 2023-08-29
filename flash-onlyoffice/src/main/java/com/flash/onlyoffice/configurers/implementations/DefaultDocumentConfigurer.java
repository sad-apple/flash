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
import com.flash.onlyoffice.domain.storage.FileStoragePathBuilder;
import com.flash.onlyoffice.domain.util.file.FileUtility;
import com.flash.onlyoffice.domain.util.service.ServiceConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Primary
public class DefaultDocumentConfigurer implements DocumentConfigurer<DefaultDocumentWrapper> {

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private FileStoragePathBuilder storagePathBuilder;

    @Autowired
    private FileUtility fileUtility;

    @Autowired
    private ServiceConverter serviceConverter;

    @Override
    public void configure(final Document document,
                          final DefaultDocumentWrapper wrapper) {  // define the document configurer
        String fileName = wrapper.getFileName();  // get the fileName parameter from the document wrapper
        Permission permission = wrapper.getPermission();  // get the permission parameter from the document wrapper

        document.setTitle(fileName);  // set the title to the document config

        // set the URL to download a file to the document config
        document.setUrl(documentManager.getDownloadUrl(fileName, true));
        // set the file URL to the document config
        document.setUrlUser(documentManager.getFileUri(fileName, false));
        document.setDirectUrl(wrapper.getIsEnableDirectUrl() ? documentManager.getDownloadUrl(fileName, false) : "");
        document.setFileType(fileUtility.getFileExtension(fileName)
                .replace(".", ""));  // set the file type to the document config
        document.getInfo().setFavorite(wrapper.getFavorite());  // set the favorite parameter to the document config

        // get the document key
        String key = serviceConverter
                .generateRevisionId(storagePathBuilder.getStorageLocation()
                        + "/" + fileName + "/"
                        + new File(storagePathBuilder.getFileLocation(fileName)).lastModified());

        document.setKey(key);  // set the key to the document config
        document.setPermissions(permission);  // set the permission parameters to the document config
    }
}
