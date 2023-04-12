package com.ndsc.biz.mongo.config;

import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;

/**
 * @Author：zhang shi ping
 * @Date：Created in 14:45 2022/3/18
 * @Description： 自定义类型映射
 * @ModifiedBy：
 * @Version:
 */
public class NoneClassMongoTypeMapper extends DefaultMongoTypeMapper {

    public NoneClassMongoTypeMapper() {
        super(null);
    }

    /*@Override
    protected TypeInformation<?> getFallbackTypeFor(Bson source) {
        if (source instanceof Document) {
            Object o = ((Document) source).get(type);
            if (o instanceof String) {
                String componentType = o.toString();
//                Class<?> aClass = FieldType.FIELDTYPE_TO_CLASS.get(componentType);
                return ClassTypeInformation.from(HashMap.class);
            }
        }
        return super.getFallbackTypeFor(source);
    }*/
}
