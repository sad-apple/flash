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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flash.onlyoffice.domain.serializers.SerializerFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zsp
 */
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentGroup {

    /**
     * 定义用户可以查看其评论的组列表
     */
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = SerializerFilter.class)
    private List<String> view;
    /**
     * 定义用户可以编辑其评论的组列表
     */
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = SerializerFilter.class)
    private List<String> edit;
    /**
     * 定义用户可以删除其评论的组列表
     */
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = SerializerFilter.class)
    private List<String> remove;

}
