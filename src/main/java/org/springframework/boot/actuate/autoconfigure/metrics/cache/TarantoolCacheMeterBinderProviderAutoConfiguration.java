package org.springframework.boot.actuate.autoconfigure.metrics.cache;

import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.metrics.cache.TarantoolCacheMeterBinderProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.tarantool.cache.TarantoolCache;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for metrics on {@link TarantoolCache TarantoolCache}.
 *
 * @author Tatiana Blinova
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(CacheManager.class)
@ConditionalOnClass({MeterBinder.class, TarantoolCache.class})
@AutoConfigureBefore(CacheMetricsAutoConfiguration.class)
@AutoConfigureAfter({MetricsAutoConfiguration.class, CacheAutoConfiguration.class})
public class TarantoolCacheMeterBinderProviderAutoConfiguration {

    @Bean
    public TarantoolCacheMeterBinderProvider tarantoolCacheMeterBinderProvider() {
        return new TarantoolCacheMeterBinderProvider();
    }
}
