package org.springframework.boot.actuate.autoconfigure.tarantool;

import org.springframework.boot.actuate.autoconfigure.health.CompositeReactiveHealthContributorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.tarantool.TarantoolReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.tarantool.TarantoolReactiveDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.tarantool.core.ReactiveTarantoolTemplate;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link TarantoolReactiveHealthIndicator}.
 *
 * @author Tatiana Blinova
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ReactiveTarantoolTemplate.class, Flux.class})
@ConditionalOnBean(ReactiveTarantoolTemplate.class)
@ConditionalOnEnabledHealthIndicator("tarantool")
@AutoConfigureAfter(TarantoolReactiveDataAutoConfiguration.class)
public class TarantoolReactiveHealthContributorAutoConfiguration extends CompositeReactiveHealthContributorConfiguration<TarantoolReactiveHealthIndicator, ReactiveTarantoolTemplate> {

    @Bean
    @ConditionalOnMissingBean(name = {"tarantoolHealthIndicator", "tarantoolHealthContributor"})
    public ReactiveHealthContributor tarantoolHealthContributor(Map<String, ReactiveTarantoolTemplate> reactiveTarantoolTemplates) {
        return createContributor(reactiveTarantoolTemplates);
    }
}