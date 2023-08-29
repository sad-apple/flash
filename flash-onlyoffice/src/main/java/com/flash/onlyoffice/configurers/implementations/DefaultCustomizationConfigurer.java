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

package com.flash.onlyoffice.configurers.implementations;

import com.flash.onlyoffice.configurers.wrappers.DefaultCustomizationWrapper;
import com.flash.onlyoffice.configurers.CustomizationConfigurer;
import com.flash.onlyoffice.domain.models.configurations.Customization;
import com.flash.onlyoffice.domain.models.enums.Action;
import com.flash.onlyoffice.domain.models.filemodel.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DefaultCustomizationConfigurer implements CustomizationConfigurer<DefaultCustomizationWrapper> {
    @Override
    // define the customization configurer
    public void configure(final Customization customization, final DefaultCustomizationWrapper wrapper) {
        Action action = wrapper.getAction();  // get the action parameter from the customization wrapper
        User user = wrapper.getUser();
        customization.setSubmitForm(false);  // set the submitForm parameter to the customization config
    }
}