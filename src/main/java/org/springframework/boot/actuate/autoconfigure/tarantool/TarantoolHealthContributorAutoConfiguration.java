package org.springframework.boot.actuate.autoconfigure.tarantool;

import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthContributorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.tarantool.TarantoolHealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.tarantool.TarantoolDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.tarantool.core.TarantoolTemplate;

import java.util.Map;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for {@link TarantoolHealthIndicator}.
 *
 * @author Tatiana Blinova
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(TarantoolTemplate.class)
@ConditionalOnBean(TarantoolTemplate.class)
@ConditionalOnEnabledHealthIndicator("tarantool")
@AutoConfigureAfter({TarantoolDataAutoConfiguration.class, TarantoolReactiveHealthContributorAutoConfiguration.class})
public class TarantoolHealthContributorAutoConfiguration extends CompositeHealthContributorConfiguration<TarantoolHealthIndicator, TarantoolTemplate> {

    @Bean
    @ConditionalOnMissingBean(name = {"tarantoolHealthIndicator", "tarantoolHealthContributor"})
    public HealthContributor tarantoolHealthContributor(Map<String, TarantoolTemplate> tarantoolTemplates) {
        return createContributor(tarantoolTemplates);
    }
}