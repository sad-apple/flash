package com.flash.onlyoffice.domain.managers.callback;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flash.onlyoffice.domain.managers.document.DocumentManager;
import com.flash.onlyoffice.domain.managers.jwt.JwtManager;
import com.flash.onlyoffice.domain.storage.FileStorageMutator;
import com.flash.onlyoffice.domain.storage.FileStoragePathBuilder;
import com.flash.onlyoffice.domain.util.file.FileUtility;
import com.flash.onlyoffice.domain.util.service.ServiceConverter;
import com.flash.onlyoffice.dto.Track;
import com.flash.onlyoffice.properties.DocServiceProperties;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.flash.onlyoffice.domain.util.Constants.FILE_SAVE_TIMEOUT;

/**
 * @author zsp
 * @date 2023/9/7 16:40
 */
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


    private void downloadToFile(final String url, final Path path) throws Exception {
        if (url == null || url.isEmpty()) {
            // URL isn't specified
            throw new RuntimeException("Url argument is not specified");
        }
        if (path == null) {
            // file isn't specified
            throw new RuntimeException("Path argument is not specified");
        }

        URL uri = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        connection.setConnectTimeout(FILE_SAVE_TIMEOUT);
        // get input stream of the file information from the URL
        InputStream stream = connection.getInputStream();

        int statusCode = connection.getResponseCode();
        if (statusCode != HttpStatus.OK.value()) {
            // checking status code
            connection.disconnect();
            throw new RuntimeException("Document editing service returned status: " + statusCode);
        }

        if (stream == null) {
            connection.disconnect();
            throw new RuntimeException("Input stream is null");
        }
        // update a file or create a new one
        storageMutator.createOrUpdateFile(path, stream);
    }

    @Override
    @SneakyThrows
    public void processSave(Track body, String fileDir) {
        String downloadUri = body.getUrl();
        String changesUri = body.getChangesurl();
        String key = body.getKey();

        // get current file extension
        String curExt = fileUtility.getFileExtension(fileDir);

        String fileLocation = storagePathBuilder.getFileLocation(fileDir);
        Path filePath = Paths.get(fileLocation);

        // 如果最后一个文件版本存在
        if (!filePath.toFile().exists()) {
            return;
        }
        Path histDir = Paths.get(storagePathBuilder.getHistoryDir(fileLocation));
        storageMutator.createDirectory(histDir);

        // 获取文件版本目录
        String versionDir = documentManager.versionDir(histDir.toAbsolutePath().toString(),
                                                       storagePathBuilder.getFileVersion(histDir.toAbsolutePath().toString(), false));


        // create the file version directory
        storageMutator.createDirectory(Paths.get(versionDir));

        // move the last file version to the file version directory with the "prev" postfix
        storageMutator.moveFile(filePath, Paths.get(versionDir + "/" + "prev" + curExt));

        // save file to the storage path
        downloadToFile(downloadUri, filePath);
        // save file changes to the diff.zip archive
        downloadToFile(changesUri, Path.of(versionDir + "/" + "diff.zip"));

        // create a json object for document changes
        JSONObject jsonChanges = new JSONObject();
        // put the changes to the json object
        jsonChanges.put("changes", body.getHistory().getChanges());
        // put the server version to the json object
        jsonChanges.put("serverVersion", body.getHistory().getServerVersion());
        String history = objectMapper.writeValueAsString(jsonChanges);

        if (history == null && body.getHistory() != null) {
            history = objectMapper.writeValueAsString(body.getHistory());
        }

        if (history != null && !history.isEmpty()) {
            // write the history changes to the changes.json file
            storageMutator.writeToFile(versionDir + "/" + "changes.json", history);
        }

        // write the key value to the key.txt file
        storageMutator.writeToFile(versionDir + "/" + "key.txt", key);

        // get the path to the forcesaved file version and remove it
        storageMutator.deleteFile(storagePathBuilder.getForcesavePath(fileLocation, false));
    }

    @Override
    @SneakyThrows
    public void commandRequest(String method, String key, HashMap meta) {
        String documentCommandUrl = docServiceProperties.getCommandUrl();

        URL url = new URL(documentCommandUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        HashMap<String, Object> params = new HashMap<>(8);
        params.put("c", method);
        params.put("key", key);

        if (meta != null) {
            params.put("meta", meta);
        }

        String headerToken;
        // check if a secret key to generate token exists or not
        if (jwtManager.tokenEnabled()) {
            Map<String, Object> payloadMap = new HashMap<>(8);
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
        JSONObject response = serviceConverter.convertStringToJson(jsonString);
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
    public void processForceSave(Track body, String fileDir) {
        String downloadUri = body.getUrl();
        String fileLocation = storagePathBuilder.getFileLocation(fileDir);
        String forcesavePath = storagePathBuilder.getForcesavePath(fileLocation, false);
        if (forcesavePath.isEmpty()) {
            forcesavePath = storagePathBuilder.getForcesavePath(fileLocation, true);
        }

        downloadToFile(downloadUri, Path.of(forcesavePath));
    }

}
