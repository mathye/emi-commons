package emi.commons.jdbc.shard.config;

/**
 * Configuration constants
 *
 * @author mathye
 * @date 2020-01-01
 */
public interface ShardStrategyConfigUtils {

    /**
     * bean name of ShardTableStrategyRegistry
     */
    String SHARD_STRATEGY_REGISTRY_BEAN_NAME =
            "ShardStrategyRegistry";

    /**
     * bean name of default ShardTableStrategy
     */
    String DEFAULT_SHARD_STRATEGY_BEAN_NAME =
            "DefaultShardStrategy";
}
