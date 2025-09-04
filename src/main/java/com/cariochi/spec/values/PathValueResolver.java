package com.cariochi.spec.values;

import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@RequiredArgsConstructor
public class PathValueResolver implements UnaryOperator<String> {

    private final WebRequest request;

    @Override
    public String apply(String name) {
        return Optional.ofNullable(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, SCOPE_REQUEST))
                .map(Map.class::cast)
                .map(m -> m.get(name))
                .map(Object::toString)
                .orElse(null);
    }
}
