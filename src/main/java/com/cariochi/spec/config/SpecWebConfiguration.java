package com.cariochi.spec.config;

import com.cariochi.spec.data.MetaPathResolver;
import com.cariochi.spec.data.PathResolver;
import com.cariochi.spec.operator.Contains;
import com.cariochi.spec.operator.ContainsIgnoreCase;
import com.cariochi.spec.operator.Equal;
import com.cariochi.spec.operator.GreaterThan;
import com.cariochi.spec.operator.GreaterThanOrEqualTo;
import com.cariochi.spec.operator.In;
import com.cariochi.spec.operator.IsNotNull;
import com.cariochi.spec.operator.IsNull;
import com.cariochi.spec.operator.LessThan;
import com.cariochi.spec.operator.LessThanOrEqualTo;
import com.cariochi.spec.operator.NotEqual;
import com.cariochi.spec.operator.NotIn;
import com.cariochi.spec.operator.StartWith;
import com.cariochi.spec.operator.StartWithIgnoreCase;
import com.cariochi.spec.web.SpecArgumentResolver;
import java.util.List;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

    @Bean
    @ConditionalOnMissingBean(PathResolver.class)
    public <T, V> PathResolver<T, V> pathResolver() {
        return new MetaPathResolver<>();
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
