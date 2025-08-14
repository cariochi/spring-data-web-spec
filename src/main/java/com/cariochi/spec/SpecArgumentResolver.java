package com.cariochi.spec;

import com.cariochi.reflecto.types.ReflectoType;
import com.cariochi.spec.Spec.AccessControl;
import com.cariochi.spec.Spec.PathVariable;
import com.cariochi.spec.Spec.RequestHeader;
import com.cariochi.spec.Spec.RequestParam;
import com.cariochi.spec.SpecOperator.SpecContext;
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

        ConversionService cs = binderFactory.createBinder(webRequest, null, "spring-data-spec").getConversionService();

        List<Specification<Object>> specs = new ArrayList<>();

        // 1) query params
        for (RequestParam qp : getRequestParams(parameter)) {
            String[] rawValues = webRequest.getParameterValues(qp.name());
            if (rawValues == null) {
                if (qp.required()) {
                    throw new IllegalArgumentException("Required request parameter '" + qp.name() + "' is missing");
                }
                continue;
            }
            String raw = join(",", rawValues);
            String path = isNotBlank(qp.path()) ? qp.path() : qp.name();
            specs.add(buildSpecification(path, raw, qp.operator(), cs));
        }

        // 2) path variables
        Map<String, String> uriVars = (Map<String, String>) webRequest.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, SCOPE_REQUEST);
        if (uriVars == null) {
            uriVars = Map.of();
        }
        for (PathVariable pv : getPathVariables(parameter)) {
            String raw = uriVars.get(pv.name());
            if (raw == null) {
                if (pv.required()) {
                    throw new IllegalArgumentException("Required path variable '" + pv.name() + "' is missing");
                }
                continue;
            }
            String path = isNotBlank(pv.path()) ? pv.path() : pv.name();
            specs.add(buildSpecification(path, raw, pv.operator(), cs));
        }

        // 3) headers
        for (RequestHeader h : getHeaders(parameter)) {
            String[] rawValues = webRequest.getHeaderValues(h.name());
            if (rawValues == null) {
                if (h.required()) {
                    throw new IllegalArgumentException("Required header '" + h.name() + "' is missing");
                }
                continue;
            }
            String raw = join(",", rawValues);
            String path = isNotBlank(h.path()) ? h.path() : h.name();
            specs.add(buildSpecification(path, raw, h.operator(), cs));
        }

        // 4) security context
        for (AccessControl sc : getAccessControls(parameter)) {
            Object value = getBean(sc.valueSupplier()).get();
            if (value == null) {
                if (sc.required()) {
                    throw new IllegalArgumentException("Required access control value is missing");
                }
                continue;
            }
            specs.add(buildSpecification(sc.path(), value, sc.operator(), cs));
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

    private <T, Y, V> Specification<T> buildSpecification(String path,
                                                          Object value,
                                                          Class<? extends SpecOperator> operatorType,
                                                          ConversionService conversionService) {
        final SpecOperator<T, Y, V> operator = getBean(operatorType);
        final SpecContext<T, Y, V> context = new SpecContext<>(path, typeDescriptor -> (V) conversionService.convert(value, typeDescriptor));
        return operator.buildSpecification(context);
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
