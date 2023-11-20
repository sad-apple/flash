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

package com.flash.onlyoffice.domain.util.file;

import com.flash.onlyoffice.domain.models.enums.DocumentType;
import com.flash.onlyoffice.properties.DocServiceProperties;
import com.flash.onlyoffice.properties.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.flash.onlyoffice.domain.util.Constants.MAX_FILE_SIZE;

/**
 * @author zhangsp
 */
@Component
@Qualifier("default")
public class DefaultFileUtility implements FileUtility {
    @Autowired
    private DocServiceProperties docServiceProperties;
    @Autowired
    private FileStorageProperties fileStorageProperties;

    /**
     * document extensions
     */
    private final List<String> extsDocument = Arrays.asList(
                            ".doc", ".docx", ".docm",
                            ".dot", ".dotx", ".dotm",
                            ".odt", ".fodt", ".ott", ".rtf", ".txt",
                            ".html", ".htm", ".mht", ".xml",
                            ".pdf", ".djvu", ".fb2", ".epub", ".xps", ".oform");

    private final List<String> extsSpreadsheet = Arrays.asList(
                            ".xls", ".xlsx", ".xlsm", ".xlsb",
                            ".xlt", ".xltx", ".xltm",
                            ".ods", ".fods", ".ots", ".csv");

    /**
     * presentation extensions
     */
    private final List<String> extsPresentation = Arrays.asList(
                            ".pps", ".ppsx", ".ppsm",
                            ".ppt", ".pptx", ".pptm",
                            ".pot", ".potx", ".potm",
                            ".odp", ".fodp", ".otp");

    /**
     * get the document type
     */
    @Override
    public DocumentType getDocumentType(final String fileName) {
        // get file extension from its name
        String ext = getFileExtension(fileName).toLowerCase();
        // word type for document extensions
        if (extsDocument.contains(ext)) {
            return DocumentType.word;
        }

        // cell type for spreadsheet extensions
        if (extsSpreadsheet.contains(ext)) {
            return DocumentType.cell;
        }

        // slide type for presentation extensions
        if (extsPresentation.contains(ext)) {
            return DocumentType.slide;
        }

        // default file type is word
        return DocumentType.word;
    }

    /**
     * get file name from its URL
     */
    @Override
    public String getFileName(final String url) {
        if (url == null) {
            return "";
        }

        File file = new File(url);
        return file.getName();

        // get file name from the last part of URL
//        String fileName = url.substring(url.lastIndexOf('/') + 1);
//        fileName = fileName.split("\\?")[0];
//        return fileName;
    }

    /**
     * get file name without extension
     */
    @Override
    public String getFileNameWithoutExtension(final String url) {
        String fileName = getFileName(url);
        if (fileName == null) {
            return null;
        }
        String fileNameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        return fileNameWithoutExt;
    }

    @Override
    public String getFileDirWithoutExtension(String url) {
        String fileDirWithoutExt = url.substring(0, url.lastIndexOf('.'));
        return fileDirWithoutExt;
    }

    /**
     * get file extension from URL
     */
    @Override
    public String getFileExtension(final String url) {
        String fileName = getFileName(url);
        if (fileName == null) {
            return null;
        }
        String fileExt = fileName.substring(fileName.lastIndexOf("."));
        return fileExt.toLowerCase();
    }

    /**
     * get an editor internal extension
     */
    @Override
    public String getInternalExtension(final DocumentType type) {
        // .docx for word file type
        if (type.equals(DocumentType.word)) {
            return ".docx";
        }

        // .xlsx for cell file type
        if (type.equals(DocumentType.cell)) {
            return ".xlsx";
        }

        // .pptx for slide file type
        if (type.equals(DocumentType.slide)) {
            return ".pptx";
        }

        // the default file type is .docx
        return ".docx";
    }

    @Override
    public List<String> getFillExts() {
        String fillformsDocs = docServiceProperties.getFillformsDocs();
        return Arrays.asList(fillformsDocs.split("\\|"));
    }

    /**
     * get file extensions that can be viewed
     */
    @Override
    public List<String> getViewedExts() {
        String viewedDocs = docServiceProperties.getViewedDocs();
        return Arrays.asList(viewedDocs.split("\\|"));
    }

    /**
     * get file extensions that can be edited
     */
    @Override
    public List<String> getEditedExts() {
        String editedDocs = docServiceProperties.getEditedDocs();
        return Arrays.asList(editedDocs.split("\\|"));
    }

    /**
     * get file extensions that can be converted
     */
    @Override
    public List<String> getConvertExts() {
        String convertDocs = docServiceProperties.getConvertDocs();
        return Arrays.asList(convertDocs.split("\\|"));
    }

    /**
     * get all the supported file extensions
     */
    @Override
    public List<String> getFileExts() {
        List<String> res = new ArrayList<>();

        res.addAll(getViewedExts());
        res.addAll(getEditedExts());
        res.addAll(getConvertExts());
        res.addAll(getFillExts());

        return res;
    }

    /**
     * generate the file path from file directory and name
     */
    @Override
    public Path generateFilepath(final String directory, final String fullFileName) {
        // get file name without extension
        String fileName = getFileNameWithoutExtension(fullFileName);
        // get file extension
        String fileExtension = getFileExtension(fullFileName);
        // get the path to the files with the specified name
        Path path = Paths.get(directory + fullFileName);

        for (int i = 1; Files.exists(path); i++) {
            // run through all the files with the specified name
            // get a name of each file without extension and add an index to it
            fileName = getFileNameWithoutExtension(fullFileName) + "(" + i + ")";

            // create a new path for this file with the correct name and extension
            path = Paths.get(directory + fileName + fileExtension);
        }

        path = Paths.get(directory + fileName + fileExtension);
        return path;
    }

    /**
     * get maximum file size
     */
    @Override
    public long getMaxFileSize() {
        long size = Long.parseLong(fileStorageProperties.getFilesizeMax());
        return size > 0 ? size : MAX_FILE_SIZE;
    }
}
