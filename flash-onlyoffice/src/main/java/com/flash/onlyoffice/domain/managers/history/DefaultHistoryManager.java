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

package com.flash.onlyoffice.domain.managers.history;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flash.onlyoffice.domain.managers.document.DocumentManager;
import com.flash.onlyoffice.domain.managers.jwt.JwtManager;
import com.flash.onlyoffice.domain.models.filemodel.Document;
import com.flash.onlyoffice.domain.storage.FileStoragePathBuilder;
import com.flash.onlyoffice.domain.util.file.FileUtility;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * todo: Rebuild completely
 *
 * @author zsp
 */
@Component
public class DefaultHistoryManager implements HistoryManager {

    @Autowired
    private FileStoragePathBuilder storagePathBuilder;

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private FileUtility fileUtility;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public String[] getHistory(final Document document) {

        String histDir = storagePathBuilder.getHistoryDir(storagePathBuilder.getFileLocation(document.getTitle()));
        Integer curVer = storagePathBuilder.getFileVersion(histDir, false);

        if (curVer > 0) {
            // check if the current file version is greater than 0
            List<Object> hist = new ArrayList<>();
            Map<String, Object> histData = new HashMap<>(8);

            for (Integer i = 1; i <= curVer; i++) {
                // run through all the file versions
                Map<String, Object> obj = new HashMap<>(8);
                Map<String, Object> dataObj = new HashMap<>(8);
                // get the path to the given file version
                String verDir = documentManager
                        .versionDir(histDir, i, true);
                // get document key
                String key = i.equals(curVer) ? document.getKey() : readFileToEnd(new File(verDir
                                                                                           + "/" +
                                                                                           "key.txt"));
                obj.put("key", key);
                obj.put("version", i);

                if (i == 1) {
                    String createdInfo = readFileToEnd(new File(histDir
                                                                + "/" +
                                                                "createdInfo.json"));
                    JSONObject json = JSON.parseObject(createdInfo);
                    // write meta information to the object (user information and creation date)
                    obj.put("created", json.get("created"));
                    Map<String, Object> user = new HashMap<String, Object>(8);
                    user.put("id", json.get("id"));
                    user.put("name", json.get("name"));
                    obj.put("user", user);
                }

                dataObj.put("fileType", fileUtility
                        .getFileExtension(document.getTitle()).replace(".", ""));
                dataObj.put("key", key);
                dataObj.put("url", i.equals(curVer) ? document.getUrl()
                                                    :
                                   documentManager.getHistoryFileUrl(document.getTitle(), i, "prev" + fileUtility
                                           .getFileExtension(document.getTitle()), true));
                if (!"".equals(document.getDirectUrl())) {
                    dataObj.put("directUrl", i.equals(curVer) ? document.getDirectUrl()
                                                              :
                                             documentManager.getHistoryFileUrl(document.getTitle(), i,
                                                                               "prev" + fileUtility
                                                                                       .getFileExtension(
                                                                                               document.getTitle()),
                                                                               false));
                }
                dataObj.put("version", i);
                if (i > 1) {
                    extracted(document, histDir, i, obj, histData, dataObj);
                }

                if (jwtManager.tokenEnabled()) {
                    dataObj.put("token", jwtManager.createToken(dataObj));
                }

                hist.add(obj);
                histData.put(Integer.toString(i - 1), dataObj);
            }

            Map<String, Object> histObj = new HashMap<String, Object>(8);
            histObj.put("currentVersion", curVer);
            histObj.put("history", hist);

            try {
                return new String[]{objectMapper.writeValueAsString(histObj),
                                    objectMapper.writeValueAsString(histData)};
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new String[]{"", ""};
    }

    /**
     * >1
     *
     * @param document
     * @param histDir
     * @param i
     * @param obj
     * @param histData
     * @param dataObj
     */
    private void extracted(Document document, String histDir, Integer i, Map<String, Object> obj, Map<String, Object> histData, Map<String, Object> dataObj) {
        // if so, get the path to the changes.json file
        JSONObject changes = JSON.parseObject(readFileToEnd(new File(documentManager
                                                                             .versionDir(histDir, i - 1,
                                                                                         true) +
                                                                     "/" +
                                                                     "changes.json")));
        JSONObject change = changes.getJSONArray("changes").getJSONObject(0);

        // write information about changes to the object
        obj.put("changes", changes.get("changes"));
        obj.put("serverVersion", changes.get("serverVersion"));
        obj.put("created", change.get("created"));
        obj.put("user", change.get("user"));

        // get the history data from the previous file version
        Map<String, Object> prev = (Map<String, Object>) histData.get(Integer.toString(i - 2));
        Map<String, Object> prevInfo = new HashMap<String, Object>(8);
        prevInfo.put("fileType", prev.get("fileType"));
        prevInfo.put("key", prev.get("key"));
        prevInfo.put("url", prev.get("url"));
        if (!"".equals(document.getDirectUrl())) {
            prevInfo.put("directUrl", prev.get("directUrl"));
        }

        // write information about previous file version to the data object
        dataObj.put("previous", prevInfo);
        // write the path to the diff.zip archive with differences in this file version
        Integer verdiff = i - 1;
        dataObj.put("changesUrl", documentManager
                .getHistoryFileUrl(document.getTitle(), verdiff, "diff.zip", true));
    }

    private String readFileToEnd(final File file) {
        StringBuilder output = new StringBuilder();
        try {
            try (FileInputStream is = new FileInputStream(file)) {
                // read data from the source
                Scanner scanner = new Scanner(is);
                scanner.useDelimiter("\\A");
                while (scanner.hasNext()) {
                    output.append(scanner.next());
                }
                scanner.close();
            }
        } catch (Exception ignored) {
        }
        return output.toString();
    }

}
