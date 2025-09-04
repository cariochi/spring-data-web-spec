package com.cariochi.spec;

import com.cariochi.reflecto.types.ReflectoType;
import com.cariochi.spec.attributes.AttributeResolver;
import com.cariochi.spec.attributes.SpecAttribute;
import com.cariochi.spec.spel.SpecMap;
import com.cariochi.spec.values.SpecValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;
import java.util.Set;

import static com.cariochi.reflecto.Reflecto.reflect;
import static com.cariochi.spec.spel.SpecSpelEvaluator.evaluate;
import static java.util.stream.Stream.concat;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedRepeatableAnnotations;

@RequiredArgsConstructor
public class SpecificationArgumentResolver implements HandlerMethodArgumentResolver {

    private final AutowireCapableBeanFactory beanFactory;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        ReflectoType type = reflect(parameter.getGenericParameterType());
        return (type.is(Specification.class) || type.is(Optional.class) && type.arguments().list().getFirst().is(Specification.class))
                && (!getAnnotations(parameter).isEmpty());
    }

    @SneakyThrows
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        ConversionService conversionService = binderFactory.createBinder(webRequest, null, "spring-data-spec").getConversionService();

        SpecMap<Object> specs = new SpecMap<>();
        AttributeResolver<Object, Object> attributeResolver = getBean(AttributeResolver.class);

        for (Spec.Condition ann : getAnnotations(parameter)) {

            Object raw = getBean(ann.valueResolver()).apply(ann.name());

            if (ObjectUtils.isEmpty(raw)) {
                if (ann.required()) {
                    throw new IllegalArgumentException("Condition value '%s' is missing".formatted(ann.name()));
                }
                continue;
            }

            String attribute = isNotBlank(ann.attribute()) ? ann.attribute() : ann.name();
            var specPath = new SpecAttribute<>(attribute, ann.joinType(), attributeResolver);

            var specValue = new SpecValue<>(raw, conversionService::convert);
            var context = new SpecContext<>(specPath, specValue, ann.distinct());
            var specification = getBean(ann.operator()).getSpecification(context);
            specs.put(ann.name(), specification);
        }

        Specification<Object> exprSpec = null;
        Spec.Expression expression = parameter.getParameterAnnotation(Spec.Expression.class);
        if (expression != null && isNotBlank(expression.value())) {
            exprSpec = evaluate(expression.value(), specs, expression.strict());
        }

        Optional<Specification<Object>> specOptional = concat(Optional.ofNullable(exprSpec).stream(), specs.getUnused().values().stream()).reduce(Specification::and);

        ReflectoType type = reflect(parameter.getGenericParameterType());
        if (type.is(Optional.class) && type.arguments().list().getFirst().is(Specification.class)) {
            return specOptional;
        } else if (type.is(Specification.class)) {
            return specOptional.orElse(null);
        }

        throw new IllegalArgumentException("Unsupported parameter type: " + parameter.getParameterType());
    }

    private <T> T getBean(Class<T> operatorType) {
        T bean = beanFactory.getBeanProvider(operatorType).getIfAvailable();
        if (bean == null) {
            bean = beanFactory.createBean(operatorType);
        }
        return bean;
    }

    private Set<Spec.Condition> getAnnotations(MethodParameter parameter) {
        return findMergedRepeatableAnnotations(parameter.getParameter(), Spec.Condition.class);
    }

}
