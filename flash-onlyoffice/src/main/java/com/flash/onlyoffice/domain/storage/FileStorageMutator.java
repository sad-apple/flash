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

package com.flash.onlyoffice.domain.storage;

import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * 指定文件存储突变函数
 * @author zhangsp
 */
public interface FileStorageMutator {

    /**
     * 创建路径
     * @param path 路径
     */
    void createDirectory(Path path);

    /**
     * 创建文件
     * @param filePath 文件路径
     * @param stream 输入流
     * @return boolean
     */
    boolean createFile(Path filePath, InputStream stream);

    /**
     * 删除文件
     * @param fileDir 文件地址
     * @return boolean
     */
    boolean deleteFile(String fileDir);

    /**
     * 删除历史文件
     *
     * @param fileDir 文件地址
     * @return b
     */
    boolean deleteFileHistory(String fileDir);

    /**
     * 写入内容
     *
     * @param fileDir 文件地址
     * @param payload 内容
     * @return b
     */
    boolean writeToFile(String fileDir, String payload);

    /**
     * 移动文件
     * @param source 元文件
     * @param destination 目的地
     * @return b
     */
    boolean moveFile(Path source, Path destination);

    /**
     * 加载文件到资源中
     * @param fileDir 文件地址
     * @return 资源数据
     */
    Resource loadFileAsResource(String fileDir);

    /**
     * 加载历史文件到资源中
     * @param fileDir 文件地址
     * @param version 版本号
     * @param file 文件名
     * @return resource
     */
    Resource loadFileAsResourceHistory(String fileDir, String version, String file);
    /**
     * 创建文件元信息
     * @param fileDir 文件路径
     * @param uid 用户id
     * @param uname 用户名
     */
    void createMeta(String fileDir, String uid, String uname);

    /**
     * 创建或者更新文件
     * @param filePath 文件
     * @param stream 输入流
     * @return b
     */
    boolean createOrUpdateFile(Path filePath, InputStream stream);
}
