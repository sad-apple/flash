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

package com.flash.onlyoffice.domain.managers.callback;

import com.flash.onlyoffice.dto.Track;

import java.util.HashMap;

/**
 * @author zhangsp
 */
public interface CallbackManager {
    /**
     * 保存
     * @param body 实体
     * @param fileDir 文件路径
     */
    void processSave(Track body, String fileDir);  // file saving process

    /**
     * 请求
     * @param method method
     * @param key key
     * @param meta meta
     */
    void commandRequest(String method, String key, HashMap meta);  // create a command request

    /**
     *file force saving process
     * @param body body
     * @param fileDir fileDir
     */
    void processForceSave(Track body, String fileDir);  // file force saving process


}
