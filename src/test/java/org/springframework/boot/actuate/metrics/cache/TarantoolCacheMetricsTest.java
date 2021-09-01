package org.springframework.boot.actuate.metrics.cache;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.tarantool.driver.api.TarantoolClient;
import io.tarantool.driver.api.TarantoolResult;
import io.tarantool.driver.api.tuple.TarantoolTuple;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.TarantoolCacheAutoConfiguration;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ContextConsumer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.tarantool.cache.TarantoolCache;
import org.springframework.data.tarantool.cache.TarantoolCacheManager;
import org.springframework.data.tarantool.core.convert.TarantoolConverter;

import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class TarantoolCacheMetricsTest {

    private static final Tags TAGS = Tags.of("app", "test").and("cache", "test");

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestCachingConfiguration.class)
            .withConfiguration(AutoConfigurations.of(
                    TarantoolCacheAutoConfiguration.class,
                    CacheAutoConfiguration.class
            ))
            .withPropertyValues(
                    "selevinia.cache.tarantool.enabled=true",
                    "selevinia.cache.tarantool.cache-names=cache1,cache2",
                    "selevinia.cache.tarantool.enable-statistics=true"
            );

    @Test
    void cacheStatisticsAreExposed() {
        ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        TarantoolCacheAutoConfiguration.class,
                        CacheAutoConfiguration.class
                ))
                .withUserConfiguration(TestCachingConfiguration.class)
                .withPropertyValues(
                        "selevinia.cache.tarantool.enabled=true",
                        "selevinia.cache.tarantool.cache-names=cache1,cache2",
                        "selevinia.cache.tarantool.enable-statistics=true"
                );

        contextRunner.run(withCacheMetrics((cache, meterRegistry) -> {
            assertThat(meterRegistry.find("cache.size").tags(TAGS).functionCounter()).isNull();
            assertThat(meterRegistry.find("cache.gets").tags(TAGS.and("result", "hit")).functionCounter()).isNotNull();
            assertThat(meterRegistry.find("cache.gets").tags(TAGS.and("result", "miss")).functionCounter()).isNotNull();
            assertThat(meterRegistry.find("cache.gets").tags(TAGS.and("result", "pending")).functionCounter()).isNotNull();
            assertThat(meterRegistry.find("cache.evictions").tags(TAGS).functionCounter()).isNull();
            assertThat(meterRegistry.find("cache.puts").tags(TAGS).functionCounter()).isNotNull();
            assertThat(meterRegistry.find("cache.removals").tags(TAGS).functionCounter()).isNotNull();
        }));
    }

    @Test
    void cacheMetricsMatchCacheStatistics() {
        contextRunner.run((context) -> {
            TarantoolCache cache = getTestCache(context);
            TarantoolCacheMetrics cacheMetrics = new TarantoolCacheMetrics(cache, TAGS);
            assertThat(cacheMetrics.hitCount()).isEqualTo(cache.getStatistics().getHits());
            assertThat(cacheMetrics.missCount()).isEqualTo(cache.getStatistics().getMisses());
            assertThat(cacheMetrics.putCount()).isEqualTo(cache.getStatistics().getPuts());
            assertThat(cacheMetrics.size()).isNull();
            assertThat(cacheMetrics.evictionCount()).isNull();
        });
    }

    private ContextConsumer<AssertableApplicationContext> withCacheMetrics(BiConsumer<TarantoolCache, MeterRegistry> stats) {
        return (context) -> {
            TarantoolCache cache = getTestCache(context);
            SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
            new TarantoolCacheMetrics(cache, Tags.of("app", "test")).bindTo(meterRegistry);
            stats.accept(cache, meterRegistry);
        };
    }

    private TarantoolCache getTestCache(AssertableApplicationContext context) {
        assertThat(context).hasSingleBean(TarantoolCacheManager.class);
        TarantoolCacheManager cacheManager = context.getBean(TarantoolCacheManager.class);
        TarantoolCache cache = (TarantoolCache) cacheManager.getCache("test");
        assertThat(cache).isNotNull();
        return cache;
    }

    @Configuration(proxyBeanMethods = false)
    @EnableCaching
    static class TestCachingConfiguration {
        @Bean
        @SuppressWarnings("unchecked")
        TarantoolClient<TarantoolTuple, TarantoolResult<TarantoolTuple>> tarantoolClient() {
            return mock(TarantoolClient.class);
        }

        @Bean
        TarantoolConverter tarantoolConverter() {
            return mock(TarantoolConverter.class);
        }
    }
}