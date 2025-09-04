package com.cariochi.spec.values;

import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;
import java.util.function.UnaryOperator;

import static java.lang.String.join;

@RequiredArgsConstructor
public class ParamValueResolver implements UnaryOperator<String> {

    private final WebRequest request;

    @Override
    public String apply(String name) {
        return Optional.ofNullable(request.getParameterValues(name)).map(values -> join(",", values)).orElse(null);
    }
}
