package com.cariochi.spec.model;

import com.cariochi.spec.SpecOperator;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsNotNull<T, Y> implements SpecOperator<T, Y, Boolean> {

    @Override
    public Specification<T> buildSpecification(SpecContext<T, Y, Boolean> context) {
        return (root, query, cb) -> {
            Path<Y> path = context.path(root);
            Boolean isNotNull = context.valueOf(boolean.class);
            return isNotNull ? path.isNotNull() : path.isNull();
        };
    }
}
