package org.springframework.boot.actuate.autoconfigure.tarantool;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.tarantool.TarantoolHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.data.tarantool.TarantoolAutoConfiguration;
import org.springframework.boot.autoconfigure.data.tarantool.TarantoolDataAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class TarantoolHealthContributorAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TarantoolAutoConfiguration.class, TarantoolDataAutoConfiguration.class,
                    TarantoolReactiveHealthContributorAutoConfiguration.class, TarantoolHealthContributorAutoConfiguration.class));

    @Test
    void shouldCreateIndicator() {
        this.contextRunner.run((context) -> assertThat(context).hasSingleBean(TarantoolHealthIndicator.class)
                .hasBean("tarantoolHealthContributor"));
    }

    @Test
    void shouldNotCreateIndicator() {
        this.contextRunner.withPropertyValues("management.health.tarantool.enabled:false")
                .run((context) -> assertThat(context).doesNotHaveBean(TarantoolHealthIndicator.class)
                        .doesNotHaveBean("tarantoolHealthContributor"));
    }
}