package org.springframework.boot.actuate.autoconfigure.metrics.cache;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.TarantoolCacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.tarantool.TarantoolAutoConfiguration;
import org.springframework.boot.autoconfigure.data.tarantool.TarantoolConversionAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

public class TarantoolCacheMeterBinderProviderAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withPropertyValues(
                    "selevinia.cache.tarantool.enabled=true",
                    "selevinia.cache.tarantool.cache-names=cache1,cache2"
            )
            .withUserConfiguration(CachingConfiguration.class)
            .withConfiguration(
                    AutoConfigurations.of(
                            TarantoolAutoConfiguration.class, TarantoolConversionAutoConfiguration.class,
                            TarantoolCacheAutoConfiguration.class,
                            CacheAutoConfiguration.class,
                            TarantoolCacheMeterBinderProviderAutoConfiguration.class,
                            MetricsAutoConfiguration.class,
                            CompositeMeterRegistryAutoConfiguration.class,
                            SimpleMetricsExportAutoConfiguration.class,
                            CacheMetricsAutoConfiguration.class
                    ));

    @Test
    void autoConfiguredCacheManagerIsInstrumented() {
        contextRunner.run((context) -> {
            MeterRegistry registry = context.getBean(MeterRegistry.class);
            registry.get("cache.gets").tags("name", "cache1").tags("cacheManager", "cacheManager").meter();
            registry.get("cache.gets").tags("name", "cache2").tags("cacheManager", "cacheManager").meter();
        });
    }

    @Configuration(proxyBeanMethods = false)
    @EnableCaching
    static class CachingConfiguration {
    }
}