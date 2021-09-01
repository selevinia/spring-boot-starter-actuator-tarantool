package org.springframework.boot.actuate.metrics.cache;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.data.tarantool.cache.TarantoolCache;

/**
 * {@link CacheMeterBinderProvider} implementation for Tarantool.
 *
 * @author Tatiana Blinova
 */
public class TarantoolCacheMeterBinderProvider implements CacheMeterBinderProvider<TarantoolCache> {

    @Override
    public MeterBinder getMeterBinder(TarantoolCache cache, Iterable<Tag> tags) {
        return new TarantoolCacheMetrics(cache, tags);
    }
}