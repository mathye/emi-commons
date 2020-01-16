package emi.commons.jdbc.shard.annotations;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启分表策略
 *
 * @author mathye
 * @date 2020-01-01
 * @see ShardTable
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ShardTableBootstrapConfiguration.class)
public @interface EnableShardTable {
}
