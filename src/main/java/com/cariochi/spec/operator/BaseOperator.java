package com.cariochi.spec.operator;

import com.cariochi.spec.data.SpecContext;
import com.cariochi.spec.data.SpecPath;
import com.cariochi.spec.data.SpecValue;
import org.springframework.data.jpa.domain.Specification;

public interface BaseOperator<T, Y, V> extends SpecOperator<T, Y, V> {

    default Specification<T> getSpecification(SpecContext<T, Y, V> context) {
        return (root, query, cb) ->
        {
            if (context.distinct()) {
                query = query.distinct(true);
            }
            return buildSpecification(context.path(), context.value()).toPredicate(root, query, cb);
        };
    }

    Specification<T> buildSpecification(SpecPath<T, Y> specPath, SpecValue<V> specValue);

}
