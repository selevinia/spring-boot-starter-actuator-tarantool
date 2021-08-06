package org.springframework.boot.actuate.tarantool;

import io.tarantool.driver.InvalidVersionException;
import io.tarantool.driver.TarantoolVersion;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.tarantool.TarantoolServerConnectionException;
import org.springframework.data.tarantool.core.ReactiveTarantoolTemplate;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TarantoolReactiveHealthIndicatorTest {

    private final ReactiveTarantoolTemplate reactiveTarantoolTemplate = mock(ReactiveTarantoolTemplate.class);
    private final ReactiveHealthIndicator reactiveHealthIndicator = new TarantoolReactiveHealthIndicator(reactiveTarantoolTemplate);

    @Test
    void shouldTestTarantoolIsUp() {
        TarantoolVersion tarantoolVersion = null;
        try {
            tarantoolVersion = TarantoolVersion.fromString("Tarantool 2.9.0");
        } catch (InvalidVersionException e) {
            fail(e);
        }
        when(reactiveTarantoolTemplate.getVersion()).thenReturn(tarantoolVersion);

        reactiveHealthIndicator.getHealth(true).as(StepVerifier::create)
                .assertNext(health -> {
                    assertThat(health).isNotNull();
                    assertThat(health.getStatus()).isEqualTo(Status.UP);
                    assertThat(health.getDetails().get("version")).isEqualTo("Tarantool 2.9.0");
                })
                .verifyComplete();
    }

    @Test
    void shouldTestTarantoolIsDown() {
        when(reactiveTarantoolTemplate.getVersion()).thenThrow(TarantoolServerConnectionException.class);

        reactiveHealthIndicator.getHealth(true).as(StepVerifier::create)
                .assertNext(health -> {
                    assertThat(health).isNotNull();
                    assertThat(health.getStatus()).isEqualTo(Status.DOWN);
                })
                .verifyComplete();
    }
}