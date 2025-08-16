package com.cariochi.spec.operator;

import com.cariochi.spec.data.SpecPath;
import com.cariochi.spec.data.SpecValue;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsNotNull<T, Y> implements BaseOperator<T, Y, Boolean> {

    @Override
    public Specification<T> buildSpecification(SpecPath<T, Y> specPath, SpecValue<Boolean> specValue) {
        return (root, query, cb) -> {
            Path<Y> path = specPath.resolve(root);
            Boolean isNotNull = specValue.convertTo(boolean.class);
            return isNotNull ? path.isNotNull() : path.isNull();
        };
    }
}
