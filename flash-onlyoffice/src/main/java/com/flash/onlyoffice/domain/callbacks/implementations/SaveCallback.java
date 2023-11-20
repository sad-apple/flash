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

package com.flash.onlyoffice.domain.callbacks.implementations;

import com.flash.onlyoffice.domain.callbacks.Callback;
import com.flash.onlyoffice.domain.callbacks.Status;
import com.flash.onlyoffice.domain.managers.callback.CallbackManager;
import com.flash.onlyoffice.dto.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 正常保存，status=2
 * @author zhangsp
 */
@Component
public class SaveCallback implements Callback {

    @Autowired
    private CallbackManager callbackManager;

    @Override
    public int handle(final Track body, final String fileDir, String bizId, String bizType) {
        callbackManager.processSave(body, fileDir);
        return 0;
    }

    @Override
    public int getStatus() {
        return Status.SAVE.getCode();
    }

    @Override
    public int sort() {
        return 0;
    }

}
