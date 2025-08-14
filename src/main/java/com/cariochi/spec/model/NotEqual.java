package com.cariochi.spec.model;

import com.cariochi.spec.SpecOperator;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class NotEqual<T, Y> implements SpecOperator<T, Y, Y> {

    @Override
    public Specification<T> buildSpecification(SpecContext<T, Y, Y> context) {
        return (root, query, cb) -> {
            Path<Y> path = context.path(root);
            Y value = context.valueOf(path.getJavaType());
            return cb.notEqual(path, value);
        };
    }
}
