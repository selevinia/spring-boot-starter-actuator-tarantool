package org.springframework.boot.actuate.metrics.cache;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.cache.CacheMeterBinder;
import org.springframework.data.tarantool.cache.TarantoolCache;

/**
 * {@link CacheMeterBinder} for {@link TarantoolCache}.
 *
 * @author Tatiana Blinova
 */
public class TarantoolCacheMetrics extends CacheMeterBinder {

    private final TarantoolCache cache;

    public TarantoolCacheMetrics(TarantoolCache cache, Iterable<Tag> tags) {
        super(cache, cache.getName(), tags);
        this.cache = cache;
    }

    @Override
    protected Long size() {
        return null;
    }

    @Override
    protected long hitCount() {
        return this.cache.getStatistics().getHits();
    }

    @Override
    protected Long missCount() {
        return this.cache.getStatistics().getMisses();
    }

    @Override
    protected Long evictionCount() {
        return null;
    }

    @Override
    protected long putCount() {
        return this.cache.getStatistics().getPuts();
    }

    @Override
    protected void bindImplementationSpecificMetrics(MeterRegistry registry) {
        FunctionCounter.builder("cache.removals", this.cache, (cache) -> cache.getStatistics().getDeletes())
                .tags(getTagsWithCacheName()).description("Cache removals").register(registry);
        FunctionCounter.builder("cache.gets", this.cache, (cache) -> cache.getStatistics().getPending())
                .tags(getTagsWithCacheName()).tag("result", "pending").description("The number of pending requests")
                .register(registry);
    }
}
