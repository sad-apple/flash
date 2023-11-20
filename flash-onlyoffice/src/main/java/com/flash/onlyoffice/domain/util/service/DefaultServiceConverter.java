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

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flash.onlyoffice.domain.managers.jwt.JwtManager;
import com.flash.onlyoffice.domain.models.enums.ConvertErrorType;
import com.flash.onlyoffice.domain.storage.FileStoragePathBuilder;
import com.flash.onlyoffice.domain.util.file.FileUtility;
import com.flash.onlyoffice.dto.Convert;
import com.flash.onlyoffice.properties.DocServiceProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.flash.onlyoffice.domain.util.Constants.CONVERTATION_ERROR_MESSAGE_TEMPLATE;
import static com.flash.onlyoffice.domain.util.Constants.CONVERT_TIMEOUT_MS;
import static com.flash.onlyoffice.domain.util.Constants.FULL_LOADING_IN_PERCENT;
import static com.flash.onlyoffice.domain.util.Constants.MAX_KEY_LENGTH;

/**
 * 文件转换
 * @author zsp
 */
@Component
@Slf4j
public class DefaultServiceConverter implements ServiceConverter {
    private final String documentJwtHeader;
    private final String docServiceUrl;
    private final String docServiceUrlConverter;
    private final String docserviceTimeout;

    public DefaultServiceConverter(DocServiceProperties docServiceProperties) {
        this.documentJwtHeader = docServiceProperties.getHeader();
        this.docServiceUrl = docServiceProperties.getUrl().getSite();
        this.docServiceUrlConverter = docServiceProperties.getUrl().getConverter();
        this.docserviceTimeout = docServiceProperties.getTimeout();
    }

    private int convertTimeout;

    @Autowired
    private JwtManager jwtManager;
    @Autowired
    private FileUtility fileUtility;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FileStoragePathBuilder fileStoragePathBuilder;

    @PostConstruct
    public void init() {
        // parse the dcoument service timeout value
        int timeout = Integer.parseInt(docserviceTimeout);
        convertTimeout = timeout > 0 ? timeout : CONVERT_TIMEOUT_MS;
    }

    @SneakyThrows
    private String postToServer(final Convert body, final String headerToken) {  // send the POST request to the server
        // write the body request to the object mapper in the string format
        String bodyString = objectMapper
                .writeValueAsString(body);
        URL url;
        HttpURLConnection connection = null;
        InputStream response;
        String jsonString = null;
        // convert body string into bytes
        byte[] bodyByte = bodyString.getBytes(StandardCharsets.UTF_8);
        try {
            // set the request parameters
            url = new URL(docServiceUrl + docServiceUrlConverter);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(convertTimeout);

            // check if the token is enabled
            if (jwtManager.tokenEnabled()) {
                // set the JWT header to the request
                connection.setRequestProperty(documentJwtHeader.isBlank()
                        ? "Authorization" : documentJwtHeader, "Bearer " + headerToken);
            }

            connection.connect();

            try (OutputStream os = connection.getOutputStream()) {
                // write bytes to the output stream
                os.write(bodyByte);
                // force write data to the output stream that can be cached in the current thread
                os.flush();
            }
            // get the input stream
            response = connection.getInputStream();
            // convert the response stream into a string
            jsonString = convertStreamToString(response);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }finally {
            connection.disconnect();
        }
        return jsonString;
    }

    @Override
    public String getConvertedUri(final String documentUri, String title, final String fromExtension,
                                  final String toExtension, final String documentRevisionId,
                                  final String filePass, final Boolean isAsync, final String lang) {
        // check if the fromExtension parameter is defined; if not, get it from the document url
        String fromExt = fromExtension == null || fromExtension.isEmpty()
                ? fileUtility.getFileExtension(documentUri) : fromExtension;

        // write all the necessary parameters to the body object
        Convert body = new Convert();
        body.setLang(lang);
        body.setUrl(documentUri);
        body.setOutputtype(toExtension.replace(".", ""));
        body.setFiletype(fromExt.replace(".", ""));
        body.setTitle(title);
        body.setKey(documentRevisionId);
        body.setFilePass(filePass);
        if (isAsync) {
            body.setAsync(true);
        }

        String headerToken = "";
        if (jwtManager.tokenEnabled()) {
            HashMap<String, Object> map = new HashMap<String, Object>(8);
            map.put("region", lang);
            map.put("url", body.getUrl());
            map.put("outputtype", body.getOutputtype());
            map.put("filetype", body.getFiletype());
            map.put("title", body.getTitle());
            map.put("key", body.getKey());
            map.put("password", body.getFilePass());
            if (isAsync) {
                map.put("async", body.getAsync());
            }

            // add token to the body if it is enabled
            String token = jwtManager.createToken(map);
            body.setToken(token);

            Map<String, Object> payloadMap = new HashMap<String, Object>(8);
            // create payload object
            payloadMap.put("payload", map);
            // create header token
            headerToken = jwtManager.createToken(payloadMap);
        }

        String jsonString = postToServer(body, headerToken);

        return getResponseUri(jsonString);
    }

    @Override
    public String generateRevisionId(final String expectedKey) {
        /* if the expected key length is greater than 20
        then he expected key is hashed and a fixed length value is stored in the string format */
        String formatKey = expectedKey.length() > MAX_KEY_LENGTH
                ? Integer.toString(expectedKey.hashCode()) : expectedKey;
        String key = formatKey.replace("[^0-9-.a-zA-Z_=]", "_");

        // the resulting key length is 20 or less
        return key.substring(0, Math.min(key.length(), MAX_KEY_LENGTH));
    }

    @Override
    public String generateRevisionIdByFileDir(String fileDir) {
        String expectedKey = fileStoragePathBuilder.getFileLocation(fileDir) + "/" +
                             new File(fileStoragePathBuilder.getFileLocation(fileDir)).lastModified();
        return generateRevisionId(expectedKey);
    }

    private void processConvertServiceResponceError(final int errorCode) {
        String errorMessage = CONVERTATION_ERROR_MESSAGE_TEMPLATE + ConvertErrorType.labelOfCode(errorCode);

        throw new RuntimeException(errorMessage);
    }

    @SneakyThrows
    private String getResponseUri(final String jsonString) {
        JSONObject jsonObj = convertStringToJson(jsonString);

        Object error = jsonObj.get("error");
        if (error != null) {
            // if an error occurs
            // then get an error message
            processConvertServiceResponceError(Math.toIntExact((long) error));
        }

        // check if the conversion is completed and save the result to a variable
        Boolean isEndConvert = (Boolean) jsonObj.get("endConvert");

        Long resultPercent;
        String responseUri = null;

        if (isEndConvert) {
            // if the conversion is completed
            resultPercent = FULL_LOADING_IN_PERCENT;
            // get the file URL
            responseUri = jsonObj.getString("fileUrl");
        } else {
            // if the conversion isn't completed
            resultPercent =  jsonObj.getLong("percent");

            // get the percentage value of the conversion process
            resultPercent = resultPercent >= FULL_LOADING_IN_PERCENT ? FULL_LOADING_IN_PERCENT - 1 : resultPercent;
        }

        return resultPercent >= FULL_LOADING_IN_PERCENT ? responseUri : "";
    }

    @Override
    @SneakyThrows
    public String convertStreamToString(final InputStream stream) {
        // create an object to get incoming stream
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        // create a string builder object
        StringBuilder stringBuilder = new StringBuilder();

        // create an object to read incoming streams
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        // get incoming streams by lines
        String line = bufferedReader.readLine();

        while (line != null) {
            // concatenate strings using the string builder
            stringBuilder.append(line);
            line = bufferedReader.readLine();
        }

        return stringBuilder.toString();
    }

    @Override
    @SneakyThrows
    public JSONObject convertStringToJson(final String jsonString) {
        //        Object obj = parser.parse(jsonString);  // parse json string

        return JSON.parseObject(jsonString);
    }
}
