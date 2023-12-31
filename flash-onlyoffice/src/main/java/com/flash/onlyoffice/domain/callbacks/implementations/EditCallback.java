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
import com.flash.onlyoffice.domain.models.enums.Action;
import com.flash.onlyoffice.dto.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 正在编辑，根据行为判断是建立连接还是断开连接
 * status=1
 * @author zhangsp
 */
@Component
public class EditCallback implements Callback {

    @Autowired
    private CallbackManager callbackManager;

    @Override
    public int handle(final Track body, final String fileDir, String bizId, String bizType) {
        int result = 0;
        com.flash.onlyoffice.dto.Action action = body.getActions().get(0);
        // 用户断开连接，如果断开连接的用户不在当前连接用户中，则强制保存
        if (action.getType().equals(Action.edit)) {
            String user = action.getUserid();
            if (!body.getUsers().contains(user)) {
                String key = body.getKey();
                callbackManager.commandRequest("forcesave", key, null);

            }
        }
        return result;
    }


    @Override
    public int getStatus() {
        return Status.EDITING.getCode();
    }

    @Override
    public int sort() {
        return 0;
    }

}
