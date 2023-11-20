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

/**
 * 文档管理器
 * @author zhangsp
 */
public interface DocumentManager {

    /**
     * 如果具有该名称的文件已存在，则获取带有索引的文件名
     * @param fileName 文件名
     * @return 文件名
     */
    String getCorrectName(String fileName);

    /**
     * 获取文件路径
     * @param fileDir
     * @param forDocumentServer
     * @return 路径地址
     */
    String getFileUri(String fileDir, Boolean forDocumentServer);

    /**
     * 获取历史文件路径
     * @param fileName 文件名
     * @param version 版本
     * @param file 文件
     * @param forDocumentServer 文件服务器
     * @return 路径
     */
    String getHistoryFileUrl(String fileName, Integer version, String file, Boolean forDocumentServer);

    /**
     * 获取回调地址
     * @param fileDir 地址
     * @return str
     */
    String getCallback(String fileDir);

    /**
     * 获取回调地址
     * @param fileDir 文件地址
     * @param bizId 业务id
     * @param bizType 业务类型
     * @return 回调地址
     */
    String getCallback(String fileDir, String bizId, String bizType);

    /**
     * 获取下载地址
     * @param fileDir 文件地址
     * @param forDocumentServer 是否是给服务端使用
     * @return 下载地址
     */
    String getDownloadUrl(String fileDir, Boolean forDocumentServer);

    /**
     * 获取版本地址
     * @param path 文件路径
     * @param version 版本
     * @param historyPath 历史文件地址
     * @return str
     */
    String versionDir(String path, Integer version, boolean historyPath);

    /**
     * 获取版本地址
     * @param path 路径
     * @param version 版本
     * @return 地址
     */
    String versionDir(String path, Integer version);

    /**
     * 创建doc文件
     * @param fileDir 文件名
     * @param uid 用户id
     * @param uname 用户名
     * @return 文件地址
     */
    String createDoc(String fileDir, String uid, String uname);

    /**
     * 文件转换
     * @param fileDir 待转文件
     * @param toExtension 转换目标文件扩展名
     * @param isAsync 是否异步处理
     * @return 转换后的文件地址
     */
    String convert(String fileDir, String toExtension, Boolean isAsync);

}
