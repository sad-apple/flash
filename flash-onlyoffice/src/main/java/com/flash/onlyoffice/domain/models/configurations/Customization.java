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

package com.flash.onlyoffice.domain.models.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The parameters which allow to customize the editor interface so that it looked like your
 *  other products (if there are any) and change the presence or absence of the additional buttons,
 *   links, change logos and editor owner details.
 *   @author zhangsp
 */
@Component
@Scope("prototype")
@Getter
@Setter
public class Customization {
    /** the image file at the top left corner of the Editor header*/
    @Autowired
    private Logo logo;
    /** the settings for the Open file location menu button and upper right corner button*/
    @Autowired
    private Goback goback;
    /** if the Autosave menu option is enabled or disabled*/
    private Boolean autosave = false;
    /**if the Comments menu button is displayed or hidden*/
    private Boolean comments = true;
    /** if the additional action buttons are displayed
    in the upper part of the editor window header next to the logo (false) or in the toolbar (true) */
    private Boolean compactHeader = false;
    /** if the top toolbar type displayed is full (false) or compact (true)*/
    private Boolean compactToolbar = false;
     /** the use of functionality only compatible with the OOXML format*/
    private Boolean compatibleFeatures = false;
    /**
     * add the request for the forced file saving to the callback handler
     *      when saving the document within the document editing service
     */
    private Boolean forcesave = true;
    /**  if the Help menu button is displayed or hidden*/
    private Boolean help = true;
    /** if the right menu is displayed or hidden on first loading*/
    private Boolean hideRightMenu = false;
    /** if the editor rulers are displayed or hidden*/
    private Boolean hideRulers = false;
    /** if the Submit form button is displayed or hidden*/
    private Boolean submitForm = false;
    private Boolean about = true;
    private Boolean feedback = true;
}
