package com.cariochi.spec.operator;

import com.cariochi.spec.data.SpecPath;
import com.cariochi.spec.data.SpecValue;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GreaterThanOrEqualTo<T, Y extends Comparable<? super Y>> implements BaseOperator<T, Y, Y> {

    @Override
    public Specification<T> buildSpecification(SpecPath<T, Y> specPath, SpecValue<Y> specValue) {
        return (root, query, cb) -> {
            Path<Y> path = specPath.resolve(root);
            Y value = specValue.convertTo(path.getJavaType());
            return cb.greaterThanOrEqualTo(path, value);
        };
    }
}
