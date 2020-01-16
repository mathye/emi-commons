package emi.commons.jdbc.shard.annotations;

import emi.commons.jdbc.shard.config.ShardStrategyConfigUtils;
import emi.commons.jdbc.shard.config.ShardStrategyRegistry;
import emi.commons.jdbc.shard.strategy.DefaultShardStrategy;
import emi.commons.jdbc.shard.strategy.IShardStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分表策略注册 自动配置类
 *
 * @author mathye
 * @date 2020-01-01
 * @see EnableShardTable
 */
@Configuration
public class ShardTableBootstrapConfiguration {

    @Bean(name = ShardStrategyConfigUtils.SHARD_STRATEGY_REGISTRY_BEAN_NAME)
    public ShardStrategyRegistry defaultShardStrategyRegistry() {
        return new ShardStrategyRegistry();
    }

    @Bean(name = ShardStrategyConfigUtils.DEFAULT_SHARD_STRATEGY_BEAN_NAME)
    public IShardStrategy defaultShardStrategy() {
        return new DefaultShardStrategy();
    }
}
