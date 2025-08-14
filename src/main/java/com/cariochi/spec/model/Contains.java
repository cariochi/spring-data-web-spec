package com.cariochi.spec.model;

import com.cariochi.spec.SpecOperator;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

@Component
public class Contains<T> implements SpecOperator<T, String, String> {

    @Override
    public Specification<T> buildSpecification(SpecContext<T, String, String> context) {
        return (root, query, cb) -> {
            Path<String> path = context.path(root);
            String value = trimToEmpty(context.valueOf(String.class));
            return value.isEmpty()
                    ? cb.conjunction()
                    : cb.like(path, "%" + value + "%");
        };
    }
}
