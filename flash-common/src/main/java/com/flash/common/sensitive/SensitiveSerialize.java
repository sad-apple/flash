package com.flash.common.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.flash.common.sensitive.annotations.SensitiveFormat;
import com.flash.common.sensitive.constant.SensitiveType;
import com.flash.common.utils.SensitiveInfoUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * @author zsp
 * @date 2023/3/31 10:12
 */
public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {
    private SensitiveType sensitiveType;

    public SensitiveSerialize() {
    }

    public SensitiveSerialize(SensitiveType sensitiveType) {
        this.sensitiveType = sensitiveType;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(SensitiveInfoUtil.sensitize(sensitiveType, value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            if (Objects.equals(property.getType().getRawClass(), String.class)) {
                SensitiveFormat
                        sensitive = property.getAnnotation(SensitiveFormat.class);
                if (sensitive == null) {
                    sensitive = property.getContextAnnotation(SensitiveFormat.class);
                }
                if (sensitive != null) {
                    return new SensitiveSerialize(sensitive.value());
                }
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(null);
    }

}
