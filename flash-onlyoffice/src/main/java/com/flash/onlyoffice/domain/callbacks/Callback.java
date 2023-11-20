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

package com.flash.onlyoffice.domain.callbacks;

import com.flash.onlyoffice.dto.Track;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangsp
 */
public interface Callback {

    /**
     * handle the callback
     * @param body vo对象
     * @param fileDir 文件路径
     * @param bizId id
     * @param bizType 类型
     * @return int
     */
    int handle(Track body, String fileDir, String bizId, String bizType);


    /**
     * get document status
     * @return status
     */
    int getStatus();

    /**
     * register a callback handler
     * @param callbackHandler callback handler
     */
    @Autowired
    default void selfRegistration(CallbackHandler callbackHandler) {
        callbackHandler.register(getStatus(), this);
    }

    /**
     * 排序
     * @return int
     */
    int sort();

}
