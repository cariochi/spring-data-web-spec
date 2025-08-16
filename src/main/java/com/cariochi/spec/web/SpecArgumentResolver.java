package com.cariochi.spec.web;

import com.cariochi.reflecto.types.ReflectoType;
import com.cariochi.spec.Spec.AccessControl;
import com.cariochi.spec.Spec.PathVariable;
import com.cariochi.spec.Spec.RequestHeader;
import com.cariochi.spec.Spec.RequestParam;
import com.cariochi.spec.data.MetaPathResolver;
import com.cariochi.spec.data.SpecContext;
import com.cariochi.spec.data.SpecPath;
import com.cariochi.spec.data.SpecValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.cariochi.reflecto.Reflecto.reflect;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@RequiredArgsConstructor
public class SpecArgumentResolver implements HandlerMethodArgumentResolver {

    private final AutowireCapableBeanFactory beanFactory;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        ReflectoType type = reflect(parameter.getGenericParameterType());
        return (type.is(Specification.class) || type.is(Optional.class) && type.arguments().list().getFirst().is(Specification.class))
               && (!getRequestParams(parameter).isEmpty() || !getPathVariables(parameter).isEmpty() || !getHeaders(parameter).isEmpty());
    }

    @SneakyThrows
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        ConversionService conversionService = binderFactory.createBinder(webRequest, null, "spring-data-spec").getConversionService();

        List<Specification<Object>> specs = new ArrayList<>();

        // 1) query params
        for (RequestParam param : getRequestParams(parameter)) {
            String[] rawValues = webRequest.getParameterValues(param.name());
            if (rawValues == null) {
                if (param.required()) {
                    throw new IllegalArgumentException("Required request parameter '" + param.name() + "' is missing");
                }
                continue;
            }
            String raw = join(",", rawValues);

            String path = isNotBlank(param.path()) ? param.path() : param.name();
            var specPath = new SpecPath<>(path, new MetaPathResolver<>(param.joinType()));
            var specValue = new SpecValue<>(typeDescriptor -> conversionService.convert(raw, typeDescriptor));
            var context = new SpecContext<>(specPath, specValue, param.distinct());
            var specification = getBean(param.operator()).getSpecification(context);
            specs.add(specification);
        }

        // 2) path variables
        Map<String, String> uriVars = (Map<String, String>) webRequest.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, SCOPE_REQUEST);
        if (uriVars == null) {
            uriVars = Map.of();
        }
        for (PathVariable variable : getPathVariables(parameter)) {
            String raw = uriVars.get(variable.name());
            if (isEmpty(raw)) {
                if (variable.required()) {
                    throw new IllegalArgumentException("Required path variable '" + variable.name() + "' is missing");
                }
                continue;
            }
            String path = isNotBlank(variable.path()) ? variable.path() : variable.name();
            var specPath = new SpecPath<>(path, new MetaPathResolver<>(variable.joinType()));
            var specValue = new SpecValue<>(typeDescriptor -> conversionService.convert(raw, typeDescriptor));
            var context = new SpecContext<>(specPath, specValue, variable.distinct());
            var specification = getBean(variable.operator()).getSpecification(context);
            specs.add(specification);
        }

        // 3) headers
        for (RequestHeader header : getHeaders(parameter)) {
            String[] rawValues = webRequest.getHeaderValues(header.name());
            if (rawValues == null) {
                if (header.required()) {
                    throw new IllegalArgumentException("Required header '" + header.name() + "' is missing");
                }
                continue;
            }
            String raw = join(",", rawValues);
            String path = isNotBlank(header.path()) ? header.path() : header.name();
            var specPath = new SpecPath<>(path, new MetaPathResolver<>(header.joinType()));
            var specValue = new SpecValue<>(typeDescriptor -> conversionService.convert(raw, typeDescriptor));
            var context = new SpecContext<>(specPath, specValue, header.distinct());
            var specification = getBean(header.operator()).getSpecification(context);
            specs.add(specification);
        }

        // 4) security context
        for (AccessControl accessControl : getAccessControls(parameter)) {
            Object value = getBean(accessControl.valueSupplier()).get();
            if (value == null) {
                if (accessControl.required()) {
                    throw new IllegalArgumentException("Required access control value is missing");
                }
                continue;
            }
            var specPath = new SpecPath<>(accessControl.path(), new MetaPathResolver<>(accessControl.joinType()));
            var specValue = new SpecValue<>(typeDescriptor -> value);
            var context = new SpecContext<>(specPath, specValue, accessControl.distinct());
            var specification = getBean(accessControl.operator()).getSpecification(context);
            specs.add(specification);
        }

        Optional<Specification<Object>> specOptional = specs.stream().reduce(Specification::and);

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

    private List<RequestParam> getRequestParams(MethodParameter parameter) {
        RequestParam.Many many = parameter.getParameterAnnotation(RequestParam.Many.class);
        if (many != null) {
            return List.of(many.value());
        }
        RequestParam one = parameter.getParameterAnnotation(RequestParam.class);
        if (one != null) {
            return List.of(one);
        }
        return List.of();
    }

    private List<PathVariable> getPathVariables(MethodParameter parameter) {
        PathVariable.Many many = parameter.getParameterAnnotation(PathVariable.Many.class);
        if (many != null) {
            return List.of(many.value());
        }
        PathVariable one = parameter.getParameterAnnotation(PathVariable.class);
        if (one != null) {
            return List.of(one);
        }
        return List.of();
    }

    private List<RequestHeader> getHeaders(MethodParameter parameter) {
        RequestHeader.Many many = parameter.getParameterAnnotation(RequestHeader.Many.class);
        if (many != null) {
            return List.of(many.value());
        }
        RequestHeader one = parameter.getParameterAnnotation(RequestHeader.class);
        if (one != null) {
            return List.of(one);
        }
        return List.of();
    }

    private List<AccessControl> getAccessControls(MethodParameter parameter) {
        AccessControl.Many many = parameter.getParameterAnnotation(AccessControl.Many.class);
        if (many != null) {
            return List.of(many.value());
        }
        AccessControl one = parameter.getParameterAnnotation(AccessControl.class);
        if (one != null) {
            return List.of(one);
        }
        return List.of();
    }

}
