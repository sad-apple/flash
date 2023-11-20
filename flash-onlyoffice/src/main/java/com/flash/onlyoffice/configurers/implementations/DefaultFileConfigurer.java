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

import com.flash.onlyoffice.configurers.FileConfigurer;
import com.flash.onlyoffice.configurers.wrappers.DefaultDocumentWrapper;
import com.flash.onlyoffice.configurers.wrappers.DefaultFileWrapper;
import com.flash.onlyoffice.domain.managers.jwt.JwtManager;
import com.flash.onlyoffice.domain.models.enums.Action;
import com.flash.onlyoffice.domain.models.enums.DocumentType;
import com.flash.onlyoffice.domain.models.filemodel.CommentGroup;
import com.flash.onlyoffice.domain.models.filemodel.FileModel;
import com.flash.onlyoffice.domain.models.filemodel.Permission;
import com.flash.onlyoffice.domain.util.file.FileUtility;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangsp
 */
@Service
@Primary
public class DefaultFileConfigurer implements FileConfigurer<DefaultFileWrapper> {

    @Autowired
    private ObjectFactory<FileModel> fileModelObjectFactory;

    @Autowired
    private FileUtility fileUtility;

    @Autowired
    private JwtManager jwtManager;

//    @Autowired
//    private Mapper<com.onlyoffice.integration.entities.Permission, Permission> mapper;

    @Autowired
    private DefaultDocumentConfigurer defaultDocumentConfigurer;

    @Autowired
    private DefaultEditorConfigConfigurer defaultEditorConfigConfigurer;

    @Override
    public void configure(final FileModel fileModel, final DefaultFileWrapper wrapper) {  // define the file configurer
        if (fileModel != null) {
            // check if the file model is specified

            // get the fileName parameter from the file wrapper
            String fileDir = wrapper.getFileDir();
            // get the action parameter from the file wrapper
            Action action = wrapper.getAction();

            // get the document type of the specified file
            DocumentType documentType = fileUtility.getDocumentType(fileDir);
            // set the document type to the file model
            fileModel.setDocumentType(documentType);
            // set the platform type to the file model
            fileModel.setType(wrapper.getType());

            Permission userPermissions = wrapper.getUser()
                                                .getPermissions();

            String fileExt = fileUtility.getFileExtension(fileDir);
            boolean canEdit = fileUtility.getEditedExts().contains(fileExt);
            boolean boo = (!canEdit && action.equals(Action.edit) || action.equals(Action.fillForms));
            if (boo && fileUtility.getFillExts().contains(fileExt)) {
                canEdit = true;
                wrapper.setAction(Action.fillForms);
            }
            wrapper.setCanEdit(canEdit);

            // define the document wrapper
            DefaultDocumentWrapper documentWrapper = DefaultDocumentWrapper
                    .builder()
                    .fileDir(fileDir)
                    .permission(updatePermissions(userPermissions, action, canEdit))
                    .favorite(wrapper.getUser().getFavorite())
                    .isEnableDirectUrl(wrapper.getIsEnableDirectUrl())
                    .title(wrapper.getTitle())
                    .build();

            // define the document configurer
            defaultDocumentConfigurer.configure(fileModel.getDocument(), documentWrapper);
            // define the editorConfig configurer
            defaultEditorConfigConfigurer.configure(fileModel.getEditorConfig(), wrapper);

            Map<String, Object> map = new HashMap<>(8);
            map.put("type", fileModel.getType());
            map.put("documentType", documentType);
            map.put("document", fileModel.getDocument());
            map.put("editorConfig", fileModel.getEditorConfig());

            // create a token and set it to the file model
            fileModel.setToken(jwtManager.createToken(map));
        }
    }

    @Override
    public FileModel getFileModel(final DefaultFileWrapper wrapper) {  // get file model
        FileModel fileModel = fileModelObjectFactory.getObject();
        // and configure it
        configure(fileModel, wrapper);
        return fileModel;
    }

    private Permission updatePermissions(final Permission userPermissions, final Action action, final Boolean canEdit) {
        userPermissions.setComment(
                !action.equals(Action.view)
                && !action.equals(Action.fillForms)
                && !action.equals(Action.embedded)
                && !action.equals(Action.blockcontent)
        );

        userPermissions.setFillForms(
                !action.equals(Action.view)
                && !action.equals(Action.comment)
                && !action.equals(Action.embedded)
                && !action.equals(Action.blockcontent)
        );

        userPermissions.setReview(canEdit
                                  && (action.equals(Action.review) || action.equals(Action.edit)));

        userPermissions.setEdit(canEdit
                                && (action.equals(Action.view)
                                    || action.equals(Action.edit)
                                    || action.equals(Action.filter)
                                    || action.equals(Action.blockcontent)));

        userPermissions.setReviewGroups(List.of("NULL"));
        // set the commentGroups parameter
        userPermissions.setCommentGroups(
                new CommentGroup(List.of("NULL"),
                                 List.of("NULL"),
                                 List.of("NULL"))
        );
        userPermissions.setUserInfoGroups(List.of("NULL"));

        return userPermissions;
    }

}
