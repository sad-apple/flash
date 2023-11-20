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

import java.nio.file.Path;
import java.util.List;

/**
 * @author zhangsp
 */
public interface FileUtility {

    /**
     * get the document type
     * @param fileName 文件名
     * @return 文件类型
     */
    DocumentType getDocumentType(String fileName);

    /**
     * get file name from its URL
     * @param url 地址
     * @return 文件名
     */
    String getFileName(String url);

    /**
     * 获取没有后缀的文件名
     * @param url 地址
     * @return 文件名
     */
    String getFileNameWithoutExtension(String url);

    /**
     * 获取没有后缀的文件路径
     * @param url url
     * @return 地址
     */
    String getFileDirWithoutExtension(String url);

    /**
     * get file extension from URL
     * @param url 地址
     * @return 地址
     */
    String getFileExtension(String url);

    /**
     * getInternalExtension
     * @param type
     * @return
     */
    String getInternalExtension(DocumentType type);  // get an editor internal extension

    /**
     * getFileExts
     * @return
     */
    List<String> getFileExts();  // get all the supported file extensions

    /**
     * getFillExts
     * @return
     */
    List<String> getFillExts();  // get file extensions that can be filled

    /**
     * getViewedExts
     * @return
     */
    List<String> getViewedExts();  // get file extensions that can be viewed

    /**
     * getEditedExts
     * @return
     */
    List<String> getEditedExts();  // get file extensions that can be edited

    /**
     * getConvertExts
     * @return
     */
    List<String> getConvertExts();  // get file extensions that can be converted
    /**
     * generateFilepath
     */
    /**
     * generateFilepath
     * @param directory
     * @param fullFileName
     * @return
     */
    Path generateFilepath(String directory, String fullFileName); // generate the file path from file directory and name

    /**
     * getMaxFileSize
     * @return
     */
    long getMaxFileSize();  // get maximum file size
}
