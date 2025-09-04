package com.cariochi.spec.autoconfigure;

import com.cariochi.spec.SpecificationArgumentResolver;
import com.cariochi.spec.config.SpecWebConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@ConditionalOnMissingBean(SpecificationArgumentResolver.class)
@ConditionalOnClass({WebMvcConfigurer.class, Specification.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "cariochi.spec.web", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(SpecWebConfiguration.class)
public class SpecWebAutoConfiguration {

}
