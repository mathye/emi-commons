package emi.commons.jdbc.shard.annotations;

import java.lang.annotation.*;

/**
 * 一个mapper操作多张表，且都需要分表，使用此注解
 *
 * @author mathye
 * @date 2020-01-01
 * @see EnableShardTable
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardTables {

    ShardTable[] shardTables();
}
