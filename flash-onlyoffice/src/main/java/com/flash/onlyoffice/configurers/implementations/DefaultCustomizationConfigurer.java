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

package com.flash.onlyoffice.configurers.implementations;

import com.flash.onlyoffice.configurers.CustomizationConfigurer;
import com.flash.onlyoffice.configurers.wrappers.DefaultCustomizationWrapper;
import com.flash.onlyoffice.domain.models.configurations.Customization;
import com.flash.onlyoffice.domain.models.enums.Action;
import com.flash.onlyoffice.domain.models.filemodel.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author zsp
 */
@Service
@Primary
public class DefaultCustomizationConfigurer implements CustomizationConfigurer<DefaultCustomizationWrapper> {

    @Override
    /**
     * 定义自定义配置器
     */
    public void configure(final Customization customization, final DefaultCustomizationWrapper wrapper) {
        // 从自定义包装器获取操作参数
        Action action = wrapper.getAction();
        User user = wrapper.getUser();
        // 将submitForm参数设置为自定义配置
        customization.setSubmitForm(false);
    }

}
