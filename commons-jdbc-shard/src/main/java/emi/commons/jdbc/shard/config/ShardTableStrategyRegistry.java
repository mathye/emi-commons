package emi.commons.jdbc.shard.config;

import emi.commons.jdbc.shard.strategy.IShardTableStrategy;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分表策略注册类
 *
 * @author mathye
 * @date 2020-01-01
 */
public class ShardTableStrategyRegistry implements ApplicationListener<ContextRefreshedEvent> {

    private final static Map<String, IShardTableStrategy> STRATEGIES = new ConcurrentHashMap<>(8);

    public static IShardTableStrategy getStrategy(Class<? extends IShardTableStrategy> strategy) {
        return STRATEGIES.get(strategy.getSimpleName());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, IShardTableStrategy> beans = event.getApplicationContext().getBeansOfType(IShardTableStrategy.class);
        beans.values().forEach(strategy -> STRATEGIES.put(strategy.getClass().getSimpleName(), strategy));
    }
}
