package org.springframework.boot.actuate.tarantool;

import io.tarantool.driver.InvalidVersionException;
import io.tarantool.driver.TarantoolVersion;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.tarantool.TarantoolServerConnectionException;
import org.springframework.data.tarantool.core.TarantoolTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TarantoolHealthIndicatorTest {

    private final TarantoolTemplate tarantoolTemplate = mock(TarantoolTemplate.class);
    private final HealthIndicator healthIndicator = new TarantoolHealthIndicator(tarantoolTemplate);

    @Test
    void shouldTestTarantoolIsUp() {
        TarantoolVersion tarantoolVersion = null;
        try {
            tarantoolVersion = TarantoolVersion.fromString("Tarantool 2.9.0");
        } catch (InvalidVersionException e) {
            fail(e);
        }
        when(tarantoolTemplate.getVersion()).thenReturn(tarantoolVersion);

        Health health = healthIndicator.getHealth(true);
        assertThat(health).isNotNull();
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails().get("version")).isEqualTo("Tarantool 2.9.0");
    }

    @Test
    void shouldTestTarantoolIsDown() {
        when(tarantoolTemplate.getVersion()).thenThrow(TarantoolServerConnectionException.class);

        Health health = healthIndicator.getHealth(true);
        assertThat(health).isNotNull();
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
    }
}