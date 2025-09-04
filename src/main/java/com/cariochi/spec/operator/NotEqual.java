package com.cariochi.spec.operator;

import com.cariochi.spec.attributes.SpecAttribute;
import com.cariochi.spec.values.SpecValue;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class NotEqual<T, Y> implements BaseOperator<T, Y, Y> {

    @Override
    public Specification<T> buildSpecification(SpecAttribute<T, Y> attribute, SpecValue<Y> specValue) {
        return (root, query, cb) -> {
            Path<Y> path = attribute.resolve(root);
            Y value = specValue.convertTo(path.getJavaType());
            return cb.notEqual(path, value);
        };
    }
}
