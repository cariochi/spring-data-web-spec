package com.cariochi.spec.operator;

import com.cariochi.spec.attributes.SpecAttribute;
import com.cariochi.spec.values.SpecValue;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class IsNull<T, Y> implements BaseOperator<T, Y, Boolean> {

    @Override
    public Specification<T> buildSpecification(SpecAttribute<T, Y> attribute, SpecValue<Boolean> specValue) {
        return (root, query, cb) -> {
            Path<Y> path = attribute.resolve(root);
            Boolean isNull = specValue.convertTo(boolean.class);
            return isNull ? path.isNull() : path.isNotNull();
        };
    }
}
