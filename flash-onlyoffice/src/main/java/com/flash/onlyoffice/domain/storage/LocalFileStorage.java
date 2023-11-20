package com.flash.onlyoffice.domain.storage;

import com.alibaba.fastjson2.JSONObject;
import com.flash.onlyoffice.domain.util.file.FileUtility;
import com.flash.onlyoffice.properties.FileStorageProperties;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.flash.onlyoffice.domain.util.Constants.KILOBYTE_SIZE;

/**
 * @author zsp
 * @date 2023/9/7 15:02
 */
@Component
public class LocalFileStorage implements FileStoragePathBuilder, FileStorageMutator {

    @Autowired
    private FileStorageProperties fileStorageProperties;
    @Autowired
    private FileUtility fileUtility;

    @Getter
    private String storageAddress;

    @Override
    public void configure(String address) {
        this.storageAddress = address;
        if (this.storageAddress == null) {
            try {
                this.storageAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                this.storageAddress = "unknown_storage";
            }
        }
        this.storageAddress.replaceAll("[^0-9a-zA-Z.=]", "_");
        createDirectory(Paths.get(getStorageLocation()));
    }

    @Override
    public String getForcesavePath(String fileLocation, Boolean create) {

        String fileName = fileUtility.getFileName(fileLocation);

        String directory = fileLocation + fileStorageProperties.getHistoryPostfix() + "/";

        Path path = Paths.get(directory);
        if (!create && !Files.exists(path)) {
            return "";
        }

        createDirectory(path);

        directory = directory + fileName;
        path = Paths.get(directory);
        if (!create && !Files.exists(path)) {
            return "";
        }

        return directory;
    }

    @Override
    public boolean createOrUpdateFile(final Path filePath, final InputStream stream) {
        // 如果文件不存在则新增，存在则更新
        if (!Files.exists(filePath)) {
            return createFile(filePath, stream);
        } else {
            try {
                Files.write(filePath, stream
                        .readAllBytes());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean createFile(final Path filePath, final InputStream stream) {
        if (Files.exists(filePath)) {
            return true;
        }
        try {
            if (!Files.exists(filePath.getParent())) {
                createDirectory(filePath.getParent());
            }
            File file = Files.createFile(filePath).toFile();
            try (FileOutputStream out = new FileOutputStream(file)) {
                int read;
                final byte[] bytes = new byte[KILOBYTE_SIZE];
                while ((read = stream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getHistoryDir(final String fileLocation) {
        return fileLocation + fileStorageProperties.getHistoryPostfix();
    }

    @Override
    public int getFileVersion(final String historyPath, final Boolean ifIndexPage) {
        Path path;
        if (ifIndexPage) {
            // todo 起始页逻辑暂时不用
            path = Paths.get(getStorageLocation() + getHistoryDir(historyPath));
        } else {
            path = Paths.get(historyPath);
            if (!Files.exists(path)) {
                return 1;
            }
        }

        // run through all the files in the history directory
        try (Stream<Path> stream = Files.walk(path, 1)) {
            return stream
                    // take only directories from the history folder
                    .filter(Files::isDirectory)
                    // get file names
                    .map(Path::getFileName)
                    // and convert them into strings
                    .map(Path::toString)
                    /* convert stream into set
                     and get its size which specifies the file version */
                    .collect(Collectors.toSet()).size();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String getFileLocation(final String fileDir) {
        if (fileDir.contains("/")) {
            return getStorageLocation() + fileDir;
        }
        return getStorageLocation() + fileUtility.getFileName(fileDir);
    }

    @Override
    public boolean moveFile(final Path source, final Path destination) {
        try {
            Files.move(source, destination,
                       new StandardCopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean writeToFile(final String fileDir, final String payload) {
        try (FileWriter fw = new FileWriter(fileDir)) {
            fw.write(payload);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void createDirectory(Path path) {
        if (Files.exists(path)) {
            return;
        }
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStorageLocation() {
        String directory = this.storageAddress + "/";
        if (!Files.exists(Paths.get(directory))) {
            createDirectory(Paths.get(directory));
        }

        return directory;
    }

    @Override
    public boolean deleteFile(final String fileDir) {
        // decode a x-www-form-urlencoded string
        String fileDirNew = URLDecoder
                .decode(fileDir, StandardCharsets.UTF_8);
        if (fileDirNew.isBlank()) {
            return false;
        }

        String fileWithoutExt = fileDir.substring(0, fileDir.lastIndexOf('.'));
        Path filePath = Paths.get(fileDir);
        Path filePathWithoutExt = Paths.get(fileWithoutExt);

        boolean fileDeleted = FileSystemUtils.deleteRecursively(filePath.toFile());
        boolean fileWithoutExtDeleted = FileSystemUtils.deleteRecursively(filePathWithoutExt.toFile());

        return fileDeleted && fileWithoutExtDeleted;
    }

    @Override
    public boolean deleteFileHistory(final String fileDir) {

        Path fileHistoryPath = Paths.get(getHistoryDir(fileDir));

        return FileSystemUtils.deleteRecursively(fileHistoryPath.toFile());
    }

    @Override
    public Resource loadFileAsResource(final String fileDir) {
        // get the path where all the forcely saved file versions are saved
        String fileLocation = getForcesavePath(fileDir, false);
        // if file location is empty
        if (fileLocation.isBlank()) {
            // get it by the file name
            fileLocation = fileDir;
        }
        try {
            // get the path to the file location
            Path filePath = Paths.get(fileLocation);
            // convert the file path to URL
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Resource loadFileAsResourceHistory(final String fileDir, final String version, final String file) {

        String fileLocation = fileDir + "-hist" + "/" + version + "/" + file;

        try {
            Path filePath = Paths.get(fileLocation);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 内部访问地址
     *
     * @param forDocumentServer
     * @return
     */
    @Override
    public String getServerUrl(final Boolean forDocumentServer) {
        if (forDocumentServer) {
            return fileStorageProperties.getInnerSite();
        } else {
            return fileStorageProperties.getOutSite();
        }
    }

    @Override
    @SneakyThrows
    public void createMeta(final String fileDir, final String uid, final String uname) {
        String histDir = getHistoryDir(fileDir);

        Path path = Paths.get(histDir);
        createDirectory(path);

        // create the json object with the file metadata
        JSONObject json = new JSONObject();
        json.put("created", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()));
        json.put("id", uid);
        json.put("name", uname);

        File meta = new File(histDir + "/" + "createdInfo.json");
        try (FileWriter writer = new FileWriter(meta)) {
            writer.write(json.toJSONString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
