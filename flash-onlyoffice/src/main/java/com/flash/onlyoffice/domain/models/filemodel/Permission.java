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
import com.flash.onlyoffice.domain.models.AbstractModel;
import com.flash.onlyoffice.domain.serializers.SerializerFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 是否允许文档编辑和下载
 * @author zsp
 */
@Component
@Scope("prototype")
@Getter
@Setter
public class Permission extends AbstractModel {

    /**
     * 该文件是否可以评论
     */
    private Boolean comment = false;
    /**
     * 内容是否可以复制到剪贴板
     */
    private Boolean copy = true;
    /**
     * 文档是否可以下载或只能在线查看或编辑
     */
    private Boolean download = true;
    /**
     * 文档是否可以编辑或只能查看
     */
    private Boolean edit = true;
    /**
     * 文件是否可以打印
     */
    private Boolean print = true;
    /**
     * 是否可以填写表格
     */
    private Boolean fillForms = false;
    /**
     * 如果过滤器可以全局应用（true）影响所有
     * 其他用户，或本地（假）
     */
    private Boolean modifyFilter = true;
    /**
     * 是否可以更改内容控制设置
     */
    private Boolean modifyContentControl = true;
    /**
     * 文件是否可以审查
     */
    private Boolean review = false;
    /**
     * 是否可以使用聊天功能
     */
    private Boolean chat = true;
    /**
     * 用户可以接受/拒绝其更改的组
     */
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = SerializerFilter.class)
    private List<String> reviewGroups;
    /**
     * 用户可以编辑、删除和/或查看其评论的组
     */
    @Autowired
    private CommentGroup commentGroups;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = SerializerFilter.class)
    private List<String> userInfoGroups;

}
