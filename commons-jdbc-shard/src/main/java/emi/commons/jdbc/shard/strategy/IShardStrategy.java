package emi.commons.jdbc.shard.strategy;

import org.apache.ibatis.mapping.BoundSql;

/**
 * 分表策略
 *
 * @author mathye
 * @date 2020-01-01
 */
public interface IShardStrategy {

    /**
     * 分表后实际的表名
     *
     * @param boundSql        mapper执行原sql
     * @param tableName       待分表的表名
     * @param shardParameters 分表依据参数名称
     * @return 分表后的表名
     */
    String realTableName(BoundSql boundSql, String tableName, String[] shardParameters);
}
