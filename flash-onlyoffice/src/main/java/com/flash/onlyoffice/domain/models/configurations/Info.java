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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文档的附加参数（文档所有者、存储文档的文件夹、
 * 上传日期、共享设置）
 *
 * @author zsp
 */
@Component
@Scope("prototype")
@Getter
@Setter
public class Info {

    /**
     * 文档所有者/创建者的姓名
     */
    private String owner = "Me";
    /**
     * 收藏夹图标的突出显示状态
     */
    private Boolean favorite = null;
    /**
     * 文件上传日期
     */
    private String uploaded = getDate();

    private String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
        return simpleDateFormat.format(new Date());
    }

}
