package org.springframework.boot.actuate.tarantool;

import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.data.tarantool.core.ReactiveTarantoolTemplate;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

/**
 * A {@link ReactiveHealthIndicator} for Tarantool.
 *
 * @author Tatiana Blinova
 */
public class TarantoolReactiveHealthIndicator extends AbstractReactiveHealthIndicator {

    private final ReactiveTarantoolTemplate reactiveTarantoolTemplate;

    public TarantoolReactiveHealthIndicator(ReactiveTarantoolTemplate reactiveTarantoolTemplate) {
        super("Tarantool health check failed");

        Assert.notNull(reactiveTarantoolTemplate, "ReactiveTarantoolTemplate must not be null");
        this.reactiveTarantoolTemplate = reactiveTarantoolTemplate;
    }

    @Override
    protected Mono<Health> doHealthCheck(Health.Builder builder) {
        return Mono.just(this.reactiveTarantoolTemplate.getVersion())
                .map(tarantoolVersion -> builder.up().withDetail("version", tarantoolVersion.toString()).build());
    }
}