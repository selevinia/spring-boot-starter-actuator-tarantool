package org.springframework.boot.actuate.autoconfigure.tarantool;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.tarantool.TarantoolReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.data.tarantool.TarantoolAutoConfiguration;
import org.springframework.boot.autoconfigure.data.tarantool.TarantoolConversionAutoConfiguration;
import org.springframework.boot.autoconfigure.data.tarantool.TarantoolReactiveDataAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class TarantoolReactiveHealthContributorAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TarantoolAutoConfiguration.class, TarantoolConversionAutoConfiguration.class,
                    TarantoolReactiveDataAutoConfiguration.class, TarantoolReactiveHealthContributorAutoConfiguration.class));

    @Test
    void shouldCreateIndicator() {
        this.contextRunner.run((context) -> assertThat(context).hasSingleBean(TarantoolReactiveHealthIndicator.class)
                .hasBean("tarantoolHealthContributor"));
    }

    @Test
    void shouldNotCreateIndicator() {
        this.contextRunner.withPropertyValues("management.health.tarantool.enabled:false")
                .run((context) -> assertThat(context).doesNotHaveBean(TarantoolReactiveHealthIndicator.class)
                        .doesNotHaveBean("tarantoolHealthContributor"));
    }
}