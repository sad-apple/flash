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

package com.flash.onlyoffice.domain.models.filemodel;

import com.flash.onlyoffice.domain.models.AbstractModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author zhangsp
 */
@Component
@Scope("prototype")
@Getter
@Setter
public class User extends AbstractModel {
    private String id;
    private String name;
    private String group;
    private Permission permissions;
    private Boolean favorite;

    public void configure(final int idParam, final String nameParam, final String groupParam, final Permission permissions) {
        // the user id
        this.id = "uid-" + idParam;
        // the user name
        this.name = nameParam;
        // the group the user belongs to
        this.group = groupParam;
        this.permissions = permissions;
    }
}
