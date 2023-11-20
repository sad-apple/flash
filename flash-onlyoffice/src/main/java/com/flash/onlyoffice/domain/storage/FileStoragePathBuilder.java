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

/**
 * @author zhangsp
 */
public interface FileStoragePathBuilder {

    /**
     * 配置
     *
     * @param address 地址
     */
    void configure(String address);

    /**
     * 获取存储目录
     *
     * @return 地址
     */
    String getStorageLocation();

    /**
     * 获取指定文件的目录
     * @param fileDir 文件地址
     * @return 目录
     */
    String getFileLocation(String fileDir);

    /**
     * 获取服务器网址
     *
     * @param forDocumentServer 是否给服务端
     * @return 地址
     */
    String getServerUrl(Boolean forDocumentServer);

    /**
     * 获取历史目录
     *
     * @param fileLocation 文件路径
     * @return 地址
     */
    String getHistoryDir(String fileLocation);

    /**
     * get the file version
     *
     * @param historyPath 历史目录
     * @param ifIndexPage ifIndexPage
     * @return 版本号
     */
    int getFileVersion(String historyPath, Boolean ifIndexPage);

    /**
     * get the path where all the
     *     forcely saved file versions are saved or create it
     * @param fileLocation 文件地址
     * @param create 是否创建
     * @return 地址
     */
    String getForcesavePath(String fileLocation, Boolean create);

}
