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

import com.flash.onlyoffice.domain.models.enums.ToolbarDocked;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author zhangsp
 * The parameters which allow to change the settings
 *  which define the behavior of the buttons in the embedded mode
 */
@Component
@Scope("prototype")
@Getter
@Setter
public class Embedded {
    /** the absolute URL to the document serving as a source file for the document embedded
     into the web page */
    private String embedUrl;
    /** the absolute URL that will allow the document to be saved
     onto the user personal computer */
    private String saveUrl;
    /** the absolute URL that will allow other users to share this document*/
    private String shareUrl;
    /** the place for the embedded viewer toolbar, can be either top or bottom*/
    private ToolbarDocked toolbarDocked;
}
