package emi.commons.jdbc.shard.strategy;

import org.apache.ibatis.mapping.BoundSql;

/**
 * 默认的分表策略
 *
 * @author mathye
 * @date 2020-01-01
 */
public class DefaultShardTableStrategy implements IShardTableStrategy {

    @Override
    public String realTableName(BoundSql boundSql, String tableName, String[] shardParameters) {
        // 表名前后一致，即不分表
        return tableName;
    }
}
