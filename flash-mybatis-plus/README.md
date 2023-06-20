# mybatis plus实践



## 配置类







## 实体类映射



### 主键策略

使用@TableId注解：

```java
@TableId(type = IdType.ASSIGN_UUID)
private String id;
```

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableId {

    /**
     * 字段名（该值可无）
     */
    String value() default "";

    /**
     * 主键类型
     * {@link IdType}
     */
    IdType type() default IdType.NONE;
}
```

其中IdType类型如下：

| 值                | 描述                                                         |
| :---------------- | :----------------------------------------------------------- |
| AUTO              | 数据库 ID 自增                                               |
| NONE              | 无状态，该类型为未设置主键类型（注解里等于跟随全局，全局里约等于 INPUT）*<!--实测不填时，应该是雪花算法-->* |
| INPUT             | insert 前自行 set 主键值                                     |
| ASSIGN_ID         | 分配 ID(主键类型为 Number(Long 和 Integer)或 String)(since 3.3.0),使用接口`IdentifierGenerator`的方法`nextId`(默认实现类为`DefaultIdentifierGenerator`雪花算法) |
| ASSIGN_UUID       | 分配 UUID,主键类型为 String(since 3.3.0),使用接口`IdentifierGenerator`的方法`nextUUID`(默认 default 方法) |
| ~~ID_WORKER~~     | 分布式全局唯一 ID 长整型类型(please use `ASSIGN_ID`)         |
| ~~UUID~~          | 32 位 UUID 字符串(please use `ASSIGN_UUID`)                  |
| ~~ID_WORKER_STR~~ | 分布式全局唯一 ID 字符串类型(please use `ASSIGN_ID`)         |

> 划线部分，新版本已删除

### 如何自定义主键策略

```java
@Slf4j
@Component
public class CustomerIdGenerator implements IdentifierGenerator {

    @Override
    public Long nextId(Object entity) {
        return IdUtil.getSnowflakeNextId();
    }
}
```

实现IdentifierGenerator，重写nextId，仅支持ASSIGN_ID，返回数值类型，主要用于实现雪花算法分区。

### 自动填充

在需要自动填充的字段上添加注解@TableField(fill = FieldFill.INSERT)：

```java
@Data
public class BaseEntity<T extends Model<?>> extends Model<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
```

支持的填充策略分别如下：

```java
/**
 * 字段填充策略枚举类
 *
 * <p>
 * 判断注入的 insert 和 update 的 sql 脚本是否在对应情况下忽略掉字段的 if 标签生成
 * <if test="...">......</if>
 * 判断优先级比 {@link FieldStrategy} 高
 * </p>
 *
 */
public enum FieldFill {
    /**
     * 默认不处理
     */
    DEFAULT,
    /**
     * 插入时填充字段
     */
    INSERT,
    /**
     * 更新时填充字段
     */
    UPDATE,
    /**
     * 插入和更新时填充字段
     */
    INSERT_UPDATE
}
```

实现MetaObjectHandler自定义填充逻辑，并注册到spring bean中
```java
@Slf4j
public class CustomerMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        // 自动填充创建人
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now())
            .strictInsertFill(metaObject, "insertTime", LocalDateTime.class, LocalDateTime.now())
            .strictInsertFill(metaObject, "createBy", String.class, "zsp");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        // 自动填充修改人
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now())
            .strictUpdateFill(metaObject, "updateBy", String.class, "zsp");
    }

}
```

```java
@Configuration
@MapperScan("com.mbp.demo.mapper")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁拦截器
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new CustomerMetaObjectHandler();
    }
}
```

### 逻辑删除

在删除字段添加注解@TableLogic

```java
@Data
public class BaseEntity<T extends Model<?>> extends Model<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设置逻辑删除
     */
    @TableLogic
    private Integer isDel = 0;
}
```

默认**未删除=0、已删除=1**

也可以自定义值：

```java
/**
 * 设置逻辑删除
 */
@TableLogic(value = "true", delval = "false")
private String isDel = "true";
```

设置未删除值、删除值，注解代码如下

```java
/**
 * 表字段逻辑处理注解（逻辑删除）
 *
 * @author hubin
 * @since 2017-09-09
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableLogic {

    /**
     * 默认逻辑未删除值（该值可无、会自动获取全局配置）
     */
    String value() default "";

    /**
     * 默认逻辑删除值（该值可无、会自动获取全局配置）
     */
    String delval() default "";
}
```

### 通用枚举类







## 数据操作

### ActiveRecord模式

> `这种模式应该是留给mp4，类似jpa的这种ddd设计`

相关概念参考这篇文章：[ActiveRecord模式整理](https://developer.aliyun.com/article/329742)

mybatis plus提供这种模式的实现方式，具体实现方法就是继承Model类：

```java
public class BaseEntity<T extends Model<?>> extends Model<T> implements Serializable {
    private static final long serialVersionUID = 1L;
}
```

```java
@Data
@TableName(value ="t_student")
public class TStudent extends BaseEntity<TStudent> implements Serializable {
}
```

使用的时候直接调用方法，如下：

```java
    @PostMapping("/")
    public void create(@RequestBody TStudentDto dto) {
        TStudent student = new TStudent();
        student.setName(dto.getName());
        student.setAddressId(dto.getAddressId());

        student.insert();
//        studentService.save(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        TStudent student = new TStudent();
        student.setId(id);
        student.deleteById();
//        studentService.save(student);
    }

    @GetMapping("/{id}")
    public TStudent query(@PathVariable String id) {
        TStudent student = new TStudent();
        student.setId(id);
        student = student.selectById();
        return student;
//        studentService.save(student);
    }
```

### Service CRUD 接口

这里只提供实例，具体参考官网：[mybatisplus CRUD 接口](https://www.baomidou.com/pages/49cc81/#service-crud-%E6%8E%A5%E5%8F%A3)，下同。

```java
public interface TStudentService extends IService<TStudent> {}
```

```java
public class TStudentServiceImpl extends ServiceImpl<TStudentMapper, TStudent> implements TStudentService{}
```



### Mapper CRUD 接口

```java
public interface TStudentMapper extends BaseMapper<TStudent> {}
```

### SimpleQuery 工具类



### xml文档



### Db类

### 分页查询

参考 [分页插件](https://www.baomidou.com/pages/97710a/#paginationinnerinterceptor)

添加分页插件

```java
@Configuration
@MapperScan("com.mbp.demo.mapper")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁拦截器
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new CustomerMetaObjectHandler();
    }
}
```



## 自定义

[自定义功能](./自定义功能.md)

## 问题

- 在不同crud的工具里，逻辑删除的处理逻辑不同，有的会触发自动填充，执行update自动填充

  | 方法                                                         | 是否触发自动填充 |
  | ------------------------------------------------------------ | ---------------- |
  | BaseMapper.deleteById(T entity)                              | 是               |
  | BaseMapper.deleteById(Serializable id)                       | 否               |
  | BaseMapper.deleteByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap) | 否               |
  | BaseMapper.delete(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper) | 否               |
  | BaseMapper其他的方法                                         | 否               |
  | IService.removeById(Serializable id)                         | 是               |
  | IService.removeById(T entity)                                | 是               |
  | IService.remove(Wrapper<T> queryWrapper)                     | 否               |
  | IService其他的方法                                           | 否               |
  | Model所有的delete方法                                        | 否               |

  

- 
