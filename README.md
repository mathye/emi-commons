# 公共组件

### 一款轻量级分表插件

#### 1. 更新日志

----
   >##### **1.0.0**
   >- 基于mybatis拦截器实现的分表插件
   >- 支持单条sql中多个表分表

#### 2. Quick Start

##### 1) mybatis拦截器配置

```xml
<plugins>
   <plugin interceptor="ShardTableInterceptor"/>
</plugins>
```

##### 2) spring-boot 启动类注解

启动类上添加注解@EnableShardTable

```java
@SpringBootApplication
@EnableShardTable
public class ApplicationStarter {
    
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }
}
```

##### 3) 定义拦截策略

```java
public class MyTable1ShardStrategy implements IShardStrategy {

    @Override
    public String realTableName(BoundSql boundSql, String tableName, String[] shardParameters) {
        // 此处自定义分表策略（表名前后一致，即不分表）
        return tableName + DateTime.now().toString("_yyyyMM");
    }
}
```

##### 4) mapper上增加注解

在mapper上增加注解@ShardTable，同一个mapper需要多种分表策略使用注解@ShardTables。

示例：需要对 my_table_1 表分表

```java
/**
 * 分表示例：在类上添加注解
 */
@ShardTables(shardTables = {
        @ShardTable(tableName = "my_table_1", shardParameters = {"param1", "param2"}, strategy = MyTable1ShardStrategy.class)
})
@Mapper
interface MyTable1Mapper {
  
    MyTable1 selectByPrimaryKey(@Param("param1") int param1,
                                @Param("param2") String param2,
                                @Param("id") Long id);
}
```

