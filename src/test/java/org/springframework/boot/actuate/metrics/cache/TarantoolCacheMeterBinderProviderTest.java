package org.springframework.boot.actuate.metrics.cache;

import io.micrometer.core.instrument.binder.MeterBinder;
import org.junit.jupiter.api.Test;
import org.springframework.data.tarantool.cache.TarantoolCache;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TarantoolCacheMeterBinderProviderTest {

    @Test
    void shouldCreateTarantoolCacheProvider() {
        TarantoolCache cache = mock(TarantoolCache.class);
        given(cache.getName()).willReturn("test");
        MeterBinder meterBinder = new TarantoolCacheMeterBinderProvider().getMeterBinder(cache, Collections.emptyList());
        assertThat(meterBinder).isInstanceOf(TarantoolCacheMetrics.class);
    }
}