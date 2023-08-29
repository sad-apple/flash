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

package com.flash.onlyoffice.domain.managers.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flash.onlyoffice.domain.managers.document.DocumentManager;
import com.flash.onlyoffice.domain.managers.jwt.JwtManager;
import com.flash.onlyoffice.properties.DocServiceProperties;
import com.flash.onlyoffice.domain.storage.FileStorageMutator;
import com.flash.onlyoffice.domain.storage.FileStoragePathBuilder;
import com.flash.onlyoffice.domain.util.file.FileUtility;
import com.flash.onlyoffice.domain.util.service.ServiceConverter;
import com.flash.onlyoffice.dto.Action;
import com.flash.onlyoffice.dto.Track;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.flash.onlyoffice.domain.util.Constants.FILE_SAVE_TIMEOUT;

// todo: Refactoring
@Component
@Primary
public class DefaultCallbackManager implements CallbackManager {

    @Autowired
    private DocumentManager documentManager;
    @Autowired
    private JwtManager jwtManager;
    @Autowired
    private FileUtility fileUtility;
    @Autowired
    private FileStorageMutator storageMutator;
    @Autowired
    private FileStoragePathBuilder storagePathBuilder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ServiceConverter serviceConverter;
    @Autowired
    private DocServiceProperties docServiceProperties;

    // save file information from the URL to the file specified
    private void downloadToFile(final String url, final Path path) throws Exception {
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("Url argument is not specified");  // URL isn't specified
        }
        if (path == null) {
            throw new RuntimeException("Path argument is not specified");  // file isn't specified
        }

        URL uri = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        connection.setConnectTimeout(FILE_SAVE_TIMEOUT);
        InputStream stream = connection.getInputStream();  // get input stream of the file information from the URL

        int statusCode = connection.getResponseCode();
        if (statusCode != HttpStatus.OK.value()) {  // checking status code
            connection.disconnect();
            throw new RuntimeException("Document editing service returned status: " + statusCode);
        }

        if (stream == null) {
            connection.disconnect();
            throw new RuntimeException("Input stream is null");
        }

        storageMutator.createOrUpdateFile(path, stream);  // update a file or create a new one
    }

    @Override
    @SneakyThrows
    public void processSave(final Track body, final String fileName) {  // file saving process
        String downloadUri = body.getUrl();
        String changesUri = body.getChangesurl();
        String key = body.getKey();
        String newFileName = fileName;

        String curExt = fileUtility.getFileExtension(fileName);  // get current file extension
        String downloadExt = "." + body.getFiletype(); // get an extension of the downloaded file

        // todo: Refactoring
        // convert downloaded file to the file with the current extension if these extensions aren't equal
        if (!curExt.equals(downloadExt)) {
            try {
                String newFileUri = serviceConverter
                        .getConvertedUri(downloadUri, downloadExt, curExt,
                                         serviceConverter.generateRevisionId(downloadUri), null, false,
                                         null);  // convert a file and get URL to a new file
                if (newFileUri.isEmpty()) {
                    newFileName = documentManager
                            .getCorrectName(fileUtility.getFileNameWithoutExtension(fileName)
                                            + downloadExt);  // get the correct file name if it already exists
                } else {
                    downloadUri = newFileUri;
                }
            } catch (Exception e) {
                newFileName = documentManager
                        .getCorrectName(fileUtility.getFileNameWithoutExtension(fileName) + downloadExt);
            }
        }

        String storagePath = storagePathBuilder.getFileLocation(newFileName);  // get the path to a new file
        Path lastVersion = Paths.get(storagePathBuilder
                                             .getFileLocation(fileName));  // get the path to the last file version

        if (lastVersion.toFile().exists()) {  // if the last file version exists
            Path histDir = Paths.get(storagePathBuilder.getHistoryDir(storagePath));  // get the history directory
            storageMutator.createDirectory(histDir);  // and create it

            String versionDir = documentManager
                    .versionDir(histDir.toAbsolutePath().toString(),  // get the file version directory
                                storagePathBuilder
                                        .getFileVersion(histDir.toAbsolutePath().toString(), false), true);

            Path ver = Paths.get(versionDir);
            Path toSave = Paths.get(storagePath);

            storageMutator.createDirectory(ver);  // create the file version directory

            // move the last file version to the file version directory with the "prev" postfix
            storageMutator.moveFile(lastVersion, Paths.get(versionDir + File.separator + "prev" + curExt));

            downloadToFile(downloadUri, toSave);  // save file to the storage path
            downloadToFile(changesUri, Path
                    .of(versionDir + File.separator + "diff.zip"));  // save file changes to the diff.zip archive

            JSONObject jsonChanges = new JSONObject();  // create a json object for document changes
            jsonChanges.put("changes", body.getHistory().getChanges());  // put the changes to the json object
            jsonChanges.put("serverVersion", body.getHistory()
                                                 .getServerVersion());  // put the server version to the json object
            String history = objectMapper.writeValueAsString(jsonChanges);

            if (history == null && body.getHistory() != null) {
                history = objectMapper.writeValueAsString(body.getHistory());
            }

            if (history != null && !history.isEmpty()) {
                // write the history changes to the changes.json file
                storageMutator.writeToFile(versionDir + File.separator + "changes.json", history);
            }

            // write the key value to the key.txt file
            storageMutator.writeToFile(versionDir + File.separator + "key.txt", key);

            // get the path to the forcesaved file version and remove it
            storageMutator.deleteFile(storagePathBuilder.getForcesavePath(newFileName, false));
        }
    }

    // todo: Replace (String method) with (Enum method)
    @Override
    @SneakyThrows
    public void commandRequest(final String method,
                               final String key,
                               final HashMap meta) {  // create a command request
        String documentCommandUrl =docServiceProperties.getCommandUrl();

        URL url = new URL(documentCommandUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        HashMap<String, Object> params = new HashMap<>();
        params.put("c", method);
        params.put("key", key);

        if (meta != null) {
            params.put("meta", meta);
        }

        String headerToken;
        // check if a secret key to generate token exists or not
        if (jwtManager.tokenEnabled()) {
            Map<String, Object> payloadMap = new HashMap<>();
            payloadMap.put("payload", params);
            // encode a payload object into a header token
            headerToken = jwtManager.createToken(payloadMap);

            // add a header Authorization with a header token and Authorization prefix in it
            String jwtHeader = docServiceProperties.getHeader();
            connection.setRequestProperty("".equals(jwtHeader)
                                          ? "Authorization" : jwtHeader, "Bearer " + headerToken);

            // encode a payload object into a body token
            String token = jwtManager.createToken(params);
            params.put("token", token);
        }

        String bodyString = objectMapper.writeValueAsString(params);

        byte[] bodyByte = bodyString.getBytes(StandardCharsets.UTF_8);

        // set the request method
        connection.setRequestMethod("POST");
        // set the Content-Type header
        connection
                .setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        // set the doOutput field to true
        connection.setDoOutput(true);
        connection.connect();

        try (OutputStream os = connection.getOutputStream()) {
            // write bytes to the output stream
            os.write(bodyByte);
        }
        // get input stream
        InputStream stream = connection.getInputStream();

        if (stream == null) {
            throw new RuntimeException("Could not get an answer");
        }

        // convert stream to json string
        String jsonString = serviceConverter.convertStreamToString(stream);
        connection.disconnect();

        // convert json string to json object
        JSONObject response = serviceConverter.convertStringToJSON(jsonString);
        // todo: Add errors ENUM
        String responseCode = response.get("error").toString();
        switch (responseCode) {
            case "0":
            case "4":
                break;
            default:
                throw new RuntimeException(response.toJSONString());
        }
    }

    @Override
    @SneakyThrows
    public void processForceSave(final Track body, final String fileNameParam) {  // file force saving process

        String downloadUri = body.getUrl();
        String fileName = fileNameParam;

        String curExt = fileUtility.getFileExtension(fileName);  // get current file extension
        String downloadExt = "." + body.getFiletype();  // get an extension of the downloaded file

        boolean newFileName = false;

        // convert downloaded file to the file with the current extension if these extensions aren't equal
        // todo: Extract function
        if (!curExt.equals(downloadExt)) {
            try {
                // convert file and get URL to a new file
                String newFileUri = serviceConverter
                        .getConvertedUri(downloadUri, downloadExt, curExt, serviceConverter
                                .generateRevisionId(downloadUri), null, false, null);
                if (newFileUri.isEmpty()) {
                    newFileName = true;
                } else {
                    downloadUri = newFileUri;
                }
            } catch (Exception e) {
                newFileName = true;
            }
        }

        String forcesavePath = "";

        // todo: Use ENUMS
        // todo: Pointless toString conversion
        boolean isSubmitForm = "3".equals(body.getForcesavetype().toString());

        // todo: Extract function
        if (isSubmitForm) {  // if the form is submitted
            if (newFileName) {
                // get the correct file name if it already exists
                fileName = documentManager
                        .getCorrectName(fileUtility
                                                .getFileNameWithoutExtension(fileName) + "-form" + downloadExt);
            } else {
                fileName = documentManager
                        .getCorrectName(fileUtility.getFileNameWithoutExtension(fileName) + "-form" + curExt);
            }
            forcesavePath = storagePathBuilder.getFileLocation(fileName);  // create forcesave path if it doesn't exist
            List<Action> actions = body.getActions();
            Action action = actions.get(0);
            String user = action.getUserid();  // get the user ID
            // create meta data for the forcesaved file
            storageMutator.createMeta(fileName, user, "Filling Form");
        } else {
            if (newFileName) {
                fileName = documentManager
                        .getCorrectName(fileUtility.getFileNameWithoutExtension(fileName) + downloadExt);
            }

            forcesavePath = storagePathBuilder.getForcesavePath(fileName, false);
            if (forcesavePath.isEmpty()) {
                forcesavePath = storagePathBuilder.getForcesavePath(fileName, true);
            }
        }

        downloadToFile(downloadUri, Path.of(forcesavePath));
    }

}
