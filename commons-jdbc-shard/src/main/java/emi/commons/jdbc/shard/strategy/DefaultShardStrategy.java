package emi.commons.jdbc.shard.strategy;

import org.apache.ibatis.mapping.BoundSql;

/**
 * 分表策略示例
 *
 * @author mathye
 * @date 2020-01-01
 */
public class DefaultShardStrategy implements IShardStrategy {

    @Override
    public String realTableName(BoundSql boundSql, String tableName, String[] shardParameters) {
        // 表名前后一致，即不分表
        return tableName;
    }
}
