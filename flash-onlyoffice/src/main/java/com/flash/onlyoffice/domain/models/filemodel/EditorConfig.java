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

package com.flash.onlyoffice.domain.models.filemodel;

import com.flash.onlyoffice.domain.models.AbstractModel;
import com.flash.onlyoffice.domain.models.configurations.Customization;
import com.flash.onlyoffice.domain.models.configurations.Embedded;
import com.flash.onlyoffice.domain.models.enums.Mode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsp
 */
@Component
@Scope("prototype")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditorConfig {

    private HashMap<String, Object> actionLink = null;
    private String callbackUrl;
    private Map<String, Object> coEditing = null;
    private String createUrl;
    @Autowired
    private Customization customization;
    @Autowired
    private Embedded embedded;
    private String lang;
    private Mode mode;
    @Autowired
    private UserInfo user;
    private List<Template> templates;

    @Component
    @Scope("prototype")
    @Getter
    @Setter
    public static class UserInfo extends AbstractModel {

        private String id;
        private String name;
        private String group;

        public void configure(final int idParam, final String nameParam, final String groupParam) {
            this.id = "uid-" + idParam;
            this.name = nameParam;
            this.group = groupParam;
        }

    }

}
