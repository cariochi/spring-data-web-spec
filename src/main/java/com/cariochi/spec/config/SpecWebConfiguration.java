package com.cariochi.spec.config;

import com.cariochi.spec.web.SpecArgumentResolver;
import java.util.List;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpecWebConfiguration {

    @Bean
    public SpecArgumentResolver specArgumentResolver(AutowireCapableBeanFactory beanFactory) {
        return new SpecArgumentResolver(beanFactory);
    }

    @Bean
    public WebMvcConfigurer specWebMvcConfigurer(SpecArgumentResolver specArgumentResolver) {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                boolean alreadyAdded = resolvers.stream().anyMatch(r -> r.getClass() == specArgumentResolver.getClass());
                if (!alreadyAdded) {
                    resolvers.add(specArgumentResolver);
                }
            }
        };
    }
}
