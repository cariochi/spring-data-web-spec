package com.cariochi.spec.autoconfigure;

import com.cariochi.spec.config.SpecWebConfiguration;
import com.cariochi.spec.web.SpecArgumentResolver;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@ConditionalOnMissingBean(SpecArgumentResolver.class)
@ConditionalOnClass({WebMvcConfigurer.class, Specification.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "cariochi.spec.web", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SpecWebAutoConfiguration {

    private final SpecWebConfiguration configuration = new SpecWebConfiguration();

    @Bean
    public SpecArgumentResolver specArgumentResolver(AutowireCapableBeanFactory beanFactory) {
        return configuration.specArgumentResolver(beanFactory);
    }

    @Bean
    public WebMvcConfigurer specWebMvcConfigurer(SpecArgumentResolver specArgumentResolver) {
        return configuration.specWebMvcConfigurer(specArgumentResolver);
    }
}
