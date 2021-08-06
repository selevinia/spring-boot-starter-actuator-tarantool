package org.springframework.boot.actuate.tarantool;

import io.tarantool.driver.TarantoolVersion;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.tarantool.core.TarantoolTemplate;
import org.springframework.util.Assert;

/**
 * A {@link HealthIndicator} for Tarantool.
 *
 * @author Tatiana Blinova
 */
public class TarantoolHealthIndicator extends AbstractHealthIndicator {

    private final TarantoolTemplate tarantoolTemplate;

    public TarantoolHealthIndicator(TarantoolTemplate tarantoolTemplate) {
        super("Tarantool health check failed");

        Assert.notNull(tarantoolTemplate, "TarantoolTemplate must not be null");
        this.tarantoolTemplate = tarantoolTemplate;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        TarantoolVersion tarantoolVersion = this.tarantoolTemplate.getVersion();
        builder.up().withDetail("version", tarantoolVersion.toString());
    }
}