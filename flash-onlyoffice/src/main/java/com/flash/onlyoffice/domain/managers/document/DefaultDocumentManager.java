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

package com.flash.onlyoffice.domain.managers.document;

import com.flash.onlyoffice.domain.storage.FileStorageMutator;
import com.flash.onlyoffice.domain.storage.FileStoragePathBuilder;
import com.flash.onlyoffice.domain.util.file.FileUtility;
import com.flash.onlyoffice.domain.util.service.ServiceConverter;
import com.flash.onlyoffice.properties.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangsp
 */
@Component
@Primary
public class DefaultDocumentManager implements DocumentManager {

    @Autowired
    private FileStorageMutator storageMutator;
    @Autowired
    private FileStoragePathBuilder storagePathBuilder;
    @Autowired
    private FileUtility fileUtility;
    @Autowired
    private ServiceConverter serviceConverter;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    /**
     * 获取正确的名称
     *
     * @param fileName 文件名
     * @return
     */
    @Override
    public String getCorrectName(final String fileName) {
        String baseName = fileUtility.getFileNameWithoutExtension(fileName);
        String ext = fileUtility.getFileExtension(fileName);
        return baseName + ext;
    }

    /**
     * 这里暂时跟下载地址一样
     */
    @Override
    public String getFileUri(final String fileDir, final Boolean forDocumentServer) {
        String serverPath = storagePathBuilder.getServerUrl(forDocumentServer);

        String query = fileStorageProperties.getDownloadUrl() + "?fileDir="
                       + URLEncoder.encode(fileDir, StandardCharsets.UTF_8);

        return serverPath + query;
    }

    /**
     * get file URL
     */
    @Override
    public String getHistoryFileUrl(final String fileName, final Integer version, final String file,
                                    final Boolean forDocumentServer) {
        try {
            // get server URL
            String serverPath = storagePathBuilder.getServerUrl(forDocumentServer);
            // get the storage directory
            String hostAddress = storagePathBuilder.getStorageLocation();
            String filePathDownload = !fileName.contains(InetAddress.getLocalHost().getHostAddress()) ? fileName
                                                                                                      :
                                      fileName.substring(fileName.indexOf(InetAddress.getLocalHost().getHostAddress())
                                                         + InetAddress.getLocalHost().getHostAddress().length() + 1);
            String userAddress = forDocumentServer ? "&userAddress" + URLEncoder
                    .encode(hostAddress, StandardCharsets.UTF_8) : "";
            String filePath = serverPath + "/downloadhistory?fileName=" + URLEncoder
                    .encode(filePathDownload, StandardCharsets.UTF_8)
                              + "&ver=" + version + "&file=" + file
                              + userAddress;
            return filePath;
        } catch (UnknownHostException e) {
            return "";
        }
    }

    /**
     * get the callback URL
     */
    @Override
    public String getCallback(final String fileDir) {
        String serverPath = storagePathBuilder.getServerUrl(true);
        String callback = fileStorageProperties.getCallbackUrl();
        String query = callback + "?fileDir=" + URLEncoder.encode(fileDir, StandardCharsets.UTF_8);
        return serverPath + query;
    }

    @Override
    public String getCallback(String fileDir, String bizId, String bizType) {
        String serverPath = storagePathBuilder.getServerUrl(true);
        String callback = fileStorageProperties.getCallbackUrl();

        List<String> params = new ArrayList<>();
        if (!StringUtils.hasLength(bizId) && !StringUtils.hasLength(bizType)) {
            params.add("bizId=" + URLEncoder.encode(bizId, StandardCharsets.UTF_8));
            params.add("bizType=" + URLEncoder.encode(bizType, StandardCharsets.UTF_8));
        }
        if (!StringUtils.hasLength(fileDir)) {
            params.add("fileDir=" + URLEncoder.encode(fileDir, StandardCharsets.UTF_8));
        }
        String paramStr = String.join("&", params);
        if (!StringUtils.hasLength(paramStr)) {
            callback = callback + "?" + paramStr;
        }


        return serverPath + callback;
    }

    /**
     * get URL to download a file
     */
    @Override
    public String getDownloadUrl(final String fileDir, final Boolean isServer) {
        String serverPath = storagePathBuilder.getServerUrl(isServer);

        String query = fileStorageProperties.getDownloadUrl() + "?fileDir="
                       + URLEncoder.encode(fileDir, StandardCharsets.UTF_8);

        return serverPath + query;
    }

    /**
     * get the path to the file version by the history path and file version
     */
    @Override
    public String versionDir(final String fileDir, final Integer version, final boolean historyPath) {
        if (!historyPath) {
            return storagePathBuilder.getHistoryDir(fileDir) + version;
        }
        return fileDir + "/" + version;
    }

    @Override
    public String versionDir(String historyPath, Integer version) {
        return historyPath + "/" + version;
    }

    @Override
    public String createDoc(String fileDir, String uid, String uname) {

        String demoName = "new.docx";
        String demoPath = "assets" + "/" + "assets/new" + "/" + demoName;

        InputStream stream = Thread.currentThread()
                                   .getContextClassLoader()
                                   .getResourceAsStream(demoPath);

        if (stream == null) {
            return null;
        }
        String fileLocation = storagePathBuilder.getFileLocation(fileDir);
        // create a file in the specified directory
        storageMutator.createFile(Path.of(fileLocation), stream);
        // create meta information of the demo file
        storageMutator.createMeta(fileLocation, uid, uname);

        return fileLocation;
    }

    @Override
    public String convert(String fileDir, String toExtension, Boolean isAsync) {
        String downloadUrl = getDownloadUrl(fileDir, true);
        String fileExt = fileUtility.getFileExtension(fileDir);
        String toFileDir = null;
        try {
            if (fileUtility.getConvertExts().contains(fileExt)) {
                String key = serviceConverter.generateRevisionIdByFileDir(fileDir);
                String fileName = fileUtility.getFileName(fileDir);
                String newFileUri = serviceConverter
                        .getConvertedUri(downloadUrl, fileName, fileExt, toExtension, key, null,
                                         isAsync, "zh");

                if (newFileUri.isEmpty()) {
                    return null;
                }

                URL url = new URL(newFileUri);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // get input stream of the converted file
                InputStream stream = connection.getInputStream();

                if (stream == null) {
                    connection.disconnect();
                    throw new RuntimeException("Input stream is null");
                }
                toFileDir = fileUtility.getFileDirWithoutExtension(fileDir) + toExtension;
                storageMutator.createOrUpdateFile(Path.of(storagePathBuilder.getFileLocation(toFileDir)), stream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return toFileDir;
    }

}
