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

package com.flash.onlyoffice.domain.callbacks;

import com.flash.onlyoffice.dto.Track;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsp
 */
@Service
@Slf4j
public class CallbackHandler {

    private final Map<Integer, List<Callback>> callbackHandlers = new HashMap<>();

    public synchronized void register(final int code, final Callback callback) {
        List<Callback> callbacks = callbackHandlers.getOrDefault(code, new ArrayList<>());
        callbacks.add(callback);
        callbackHandlers.put(code, callbacks);
    }

    @Transactional(rollbackFor = Exception.class)
    public int handle(final Track body, final String fileDir, String bizId, String bizType) {

        Status of = Status.of(body.getStatus());
        if (of != null) {
            log.info("status.code={}, status.name={}, action={}", of.getCode(), of.name(), body.getActions());
        } else {
            log.info("status={}, action={}", body.getStatus(), body.getActions());
        }
        log.info(body.toString());

        List<Callback> callbacks = callbackHandlers.get(body.getStatus());
        if (CollectionUtils.isEmpty(callbacks)) {
            log.warn("Callback status " + body.getStatus() + " is not supported yet");
           return 0;
        }
        try {
            // 按顺序执行
            callbacks.sort(Comparator.comparingInt(Callback::sort));

            for (Callback callback : callbacks) {
                callback.handle(body, fileDir, bizId, bizType);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException("回调处理异常");
        }
        return 0;
    }


}
