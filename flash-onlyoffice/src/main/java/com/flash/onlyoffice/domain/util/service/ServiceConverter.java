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

package com.flash.onlyoffice.domain.util.service;

import com.alibaba.fastjson2.JSONObject;

import java.io.InputStream;

/**
 * 转换服务
 * @author zhangsp
 */
public interface ServiceConverter {
    /**
     * getConvertedUri
     * @param documentUri
     * @param title
     * @param fromExtension
     * @param toExtension
     * @param documentRevisionId
     * @param filePass
     * @param isAsync
     * @param lang
     * @return
     */
    String getConvertedUri(String documentUri, String title, String fromExtension,
                                  String toExtension, String documentRevisionId,
                                  String filePass, Boolean isAsync, String lang);

    /**
     * generateRevisionId
     * @param expectedKey
     * @return
     */
    String generateRevisionId(String expectedKey);

    /**
     * generateRevisionIdByFileDir
     * @param fileDir
     * @return
     */
    String generateRevisionIdByFileDir(String fileDir);

    /**
     * convertStreamToString
     * @param stream
     * @return
     */
    String convertStreamToString(InputStream stream);

    /**
     * convertStringToJson
     * @param jsonString
     * @return
     */
    JSONObject convertStringToJson(String jsonString);
}
