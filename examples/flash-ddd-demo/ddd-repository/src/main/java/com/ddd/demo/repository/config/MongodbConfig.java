package com.ddd.demo.repository.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @Author：zhang shi ping
 * @Date：Created in 18:30 2022/3/17
 * @Description：
 * @ModifiedBy：
 * @Version:
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.ddd.demo.repository.repository")
public class MongodbConfig {

    @Bean
    MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext context,
                                                MongoCustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(conversions);
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mappingConverter;
    }
}
