package emi.commons.jdbc.shard.mybatis;

import emi.commons.jdbc.shard.annotations.ShardTable;
import emi.commons.jdbc.shard.annotations.ShardTables;
import emi.commons.jdbc.shard.config.ShardStrategyRegistry;
import emi.commons.jdbc.shard.exceptions.TooManyShardTableResultException;
import emi.commons.jdbc.shard.strategy.IShardStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_FACTORY;
import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

/**
 * 这里分表方式是通过表名匹配、替换操作实现，若两张表表名相似，则可能结果与预期不符；
 * 为了尽量避免这种情况，在结合{@link ShardTable}注解使用时，尽量避免同一个mapper下操作表名相似的多张表
 * <p>
 * mybatis 分表拦截器
 *
 * @author mathye
 * @date 2020-01-01
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class ShardTableInterceptor implements Interceptor {

    private static Logger log = LoggerFactory.getLogger(ShardTableInterceptor.class);

    private final Pattern pattern = Pattern.compile("[\\s]+");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        //获取sql语句, 表名须小写
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originSql = pattern.matcher(boundSql.getSql()).replaceAll(" ");
        if (!StringUtils.isEmpty(originSql)) {
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
            String id = mappedStatement.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            Class<?> clazz = Class.forName(className);
            ShardTable shardTable = clazz.getAnnotation(ShardTable.class);
            ShardTables shardTables = clazz.getAnnotation(ShardTables.class);
            if (shardTable != null && shardTables != null) {
                throw new TooManyShardTableResultException("同一个mapper中ShardTable和ShardTables只能存在一个");
            }
            if (shardTable != null) {
                this.shard(originSql, boundSql, metaObject, shardTable);
            } else if (shardTables != null) {
                this.shard(originSql, boundSql, metaObject, shardTables.shardTables());
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private void shard(String originSql, BoundSql boundSql, MetaObject metaObject, ShardTable... shardTable) {
        if (shardTable == null || shardTable.length == 0) {
            return;
        }
        List<String> searchList = new ArrayList<>();
        List<String> replacementList = new ArrayList<>();
        Arrays.stream(shardTable).forEach(shard -> {
            String tableName = shard.tableName();
            String[] shardParameters = shard.shardParameters();
            Class<? extends IShardStrategy> strategyClazz = shard.strategy();
            IShardStrategy tableStrategy = ShardStrategyRegistry.getStrategy(strategyClazz);
            String realTableName = tableStrategy.realTableName(boundSql, tableName, shardParameters);
            if (!tableName.equals(realTableName)) {
                log.info("分表结果:" + tableName + "-->" + realTableName);
                // 为了避免因表名相似，导致的分表名称替换错误，在表名两端添加空格符再匹配
                searchList.add(StringUtils.wrap(tableName, " "));
                replacementList.add(StringUtils.wrap(realTableName, " "));
            }
        });
        String newSql = StringUtils.replaceEach(originSql, searchList.toArray(new String[0]), replacementList.toArray(new String[0]));
        metaObject.setValue("delegate.boundSql.sql", newSql);
    }
}
