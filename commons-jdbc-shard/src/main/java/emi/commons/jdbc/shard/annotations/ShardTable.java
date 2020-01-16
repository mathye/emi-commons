package emi.commons.jdbc.shard.annotations;


import emi.commons.jdbc.shard.strategy.IShardStrategy;

import java.lang.annotation.*;

/**
 * 代表注解所在mapper类中所有sql需要执行该分表策略
 *
 * @author mathye
 * @date 2020-01-01
 * @see EnableShardTable
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardTable {

    /**
     * 分表的表名（区分大小写，表名建议通用小写）
     */
    String tableName();

    /**
     * 分表依据的mapper方法 传参参数名称（区分大小写）
     */
    String[] shardParameters();

    /**
     * 分表策略
     */
    Class<? extends IShardStrategy> strategy();
}
