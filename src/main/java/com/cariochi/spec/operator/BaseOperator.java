package com.cariochi.spec.operator;

import com.cariochi.spec.SpecContext;
import com.cariochi.spec.attributes.SpecAttribute;
import com.cariochi.spec.values.SpecValue;
import org.springframework.data.jpa.domain.Specification;

public interface BaseOperator<T, Y, V> extends Operator<T, Y, V> {

    default Specification<T> getSpecification(SpecContext<T, Y, V> context) {
        return (root, query, cb) -> {
            if (context.distinct()) {
                query = query.distinct(true);
            }
            return buildSpecification(context.attribute(), context.specValue()).toPredicate(root, query, cb);
        };
    }

    Specification<T> buildSpecification(SpecAttribute<T, Y> attribute, SpecValue<V> specValue);

}
