
package com.flash.onlyoffice.configurers.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flash.onlyoffice.configurers.EditorConfigConfigurer;
import com.flash.onlyoffice.configurers.wrappers.DefaultCustomizationWrapper;
import com.flash.onlyoffice.configurers.wrappers.DefaultEmbeddedWrapper;
import com.flash.onlyoffice.configurers.wrappers.DefaultFileWrapper;
import com.flash.onlyoffice.domain.managers.document.DocumentManager;
import com.flash.onlyoffice.domain.managers.template.TemplateManager;
import com.flash.onlyoffice.domain.models.enums.Action;
import com.flash.onlyoffice.domain.models.enums.Mode;
import com.flash.onlyoffice.domain.models.filemodel.EditorConfig;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zsp
 */
@Service
@Primary
public class DefaultEditorConfigConfigurer implements EditorConfigConfigurer<DefaultFileWrapper> {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    @Qualifier("sample")
    private TemplateManager templateManager;

    @Autowired
    private DefaultCustomizationConfigurer defaultCustomizationConfigurer;

    @Autowired
    private DefaultEmbeddedConfigurer defaultEmbeddedConfigurer;

    /**
     * 定义 editorConfig 配置器
     *
     * @param config  配置对象
     * @param wrapper 包装类
     */
    @Override
    @SneakyThrows
    public void configure(final EditorConfig config,
                          final DefaultFileWrapper wrapper) {
        // 检查 editorConfig 包装器中的 actionData 是否不为空
        if (wrapper.getActionData() != null) {

            // 将 actionLink 设置为 editorConfig
            config.setActionLink(objectMapper.readValue(wrapper.getActionData(), new TypeReference<>() {
            }));
        }
        String fileDir = wrapper.getFileDir();
        String bizId = wrapper.getBizId();
        String bizType = wrapper.getBizType();
        // 检查 editorConfig 包装器中的用户是否是匿名的
        boolean userIsAnon = "Anonymous".equals(wrapper.getUser().getName());

        // 如果用户不是匿名的，则将模板设置为 editorConfig
        config.setTemplates(userIsAnon ? null : templateManager.createTemplates(fileDir));
        config.setCallbackUrl(documentManager.getCallback(fileDir, bizId, bizType));

        // todo 创建地址后期补充上去
        config.setCreateUrl("");
        config.setLang(wrapper.getLang());
        Boolean canEdit = wrapper.getCanEdit();
        Action action = wrapper.getAction();
        config.setCoEditing(action.equals(Action.view) && userIsAnon ?
                            Map.of("mode", "strict", "change", false) : null);

        // define the customization configurer
        defaultCustomizationConfigurer.configure(config.getCustomization(),
                                                 DefaultCustomizationWrapper.builder()
                                                                            .action(action)
                                                                            .user(userIsAnon ? null : wrapper.getUser())
                                                                            .build());
        config.setMode(canEdit && !action.equals(Action.view) ? Mode.edit : Mode.view);
        EditorConfig.UserInfo userInfo = new EditorConfig.UserInfo();
        userInfo.setId(wrapper.getUser().getId());
        userInfo.setName(wrapper.getUser().getName());
        userInfo.setGroup(wrapper.getUser().getGroup());
        config.setUser(userInfo);
        defaultEmbeddedConfigurer.configure(config.getEmbedded(), DefaultEmbeddedWrapper.builder()
                                                                                        .type(wrapper.getType())
                                                                                        .fileDir(fileDir)
                                                                                        .build());
    }

}
