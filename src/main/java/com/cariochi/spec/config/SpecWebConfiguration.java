package com.cariochi.spec.config;

import com.cariochi.spec.SpecificationArgumentResolver;
import com.cariochi.spec.attributes.AttributeResolver;
import com.cariochi.spec.attributes.MetaAttributeResolver;
import com.cariochi.spec.operator.*;
import com.cariochi.spec.values.HeaderValueResolver;
import com.cariochi.spec.values.ParamValueResolver;
import com.cariochi.spec.values.PathValueResolver;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SpecWebConfiguration {

    @Bean
    public SpecificationArgumentResolver specArgumentResolver(AutowireCapableBeanFactory beanFactory) {
        return new SpecificationArgumentResolver(beanFactory);
    }

    @Bean
    public WebMvcConfigurer specWebMvcConfigurer(SpecificationArgumentResolver specificationArgumentResolver) {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                boolean alreadyAdded = resolvers.stream().anyMatch(r -> r.getClass() == specificationArgumentResolver.getClass());
                if (!alreadyAdded) {
                    resolvers.add(specificationArgumentResolver);
                }
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(AttributeResolver.class)
    public <T, V> AttributeResolver<T, V> attributeResolver() {
        return new MetaAttributeResolver<>();
    }

    @Bean
    public PathValueResolver pathValueResolver(WebRequest webRequest) {
        return new PathValueResolver(webRequest);
    }

    @Bean
    public ParamValueResolver paramValueResolver(WebRequest webRequest) {
        return new ParamValueResolver(webRequest);
    }

    @Bean
    public HeaderValueResolver headerValueResolver(WebRequest webRequest) {
        return new HeaderValueResolver(webRequest);
    }

    @Bean
    public <T, Y> Equal<T, Y> equal() {
        return new Equal<>();
    }

    @Bean
    public <T, Y> NotEqual<T, Y> notEqual() {
        return new NotEqual<>();
    }

    @Bean
    public <T, Y> In<T, Y> in() {
        return new In<>();
    }

    @Bean
    public <T, Y> NotIn<T, Y> notIn() {
        return new NotIn<>();
    }

    @Bean
    public <T, Y> IsNull<T, Y> isNull() {
        return new IsNull<>();
    }

    @Bean
    public <T, Y> IsNotNull<T, Y> isNotNull() {
        return new IsNotNull<>();
    }

    @Bean
    public <T> StartWith<T> startWith() {
        return new StartWith<>();
    }

    @Bean
    public <T> StartWithIgnoreCase<T> startWithIgnoreCase() {
        return new StartWithIgnoreCase<>();
    }

    @Bean
    public <T> Contains<T> contains() {
        return new Contains<>();
    }

    @Bean
    public <T> ContainsIgnoreCase<T> containsIgnoreCase() {
        return new ContainsIgnoreCase<>();
    }

    @Bean
    public <T, Y extends Comparable<? super Y>> GreaterThan<T, Y> greaterThan() {
        return new GreaterThan<>();
    }

    @Bean
    public <T, Y extends Comparable<? super Y>> GreaterThanOrEqualTo<T, Y> greaterThanOrEqualTo() {
        return new GreaterThanOrEqualTo<>();
    }

    @Bean
    public <T, Y extends Comparable<? super Y>> LessThan<T, Y> lessThan() {
        return new LessThan<>();
    }

    @Bean
    public <T, Y extends Comparable<? super Y>> LessThanOrEqualTo<T, Y> lessThanOrEqualTo() {
        return new LessThanOrEqualTo<>();
    }
}
