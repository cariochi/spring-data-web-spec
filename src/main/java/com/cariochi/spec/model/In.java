package com.cariochi.spec.model;

import com.cariochi.spec.SpecOperator;
import jakarta.persistence.criteria.Path;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class In<T, Y> implements SpecOperator<T, Y, List<Y>> {

    @Override
    public Specification<T> buildSpecification(SpecContext<T, Y, List<Y>> context) {
        return (root, query, cb) -> {
            Path<Y> path = context.path(root);
            List<Y> list = context.collectionOf(path.getJavaType());
            if (list.isEmpty()) {
                return cb.disjunction();
            } else if (list.size() == 1) {
                return cb.equal(path, list.getFirst());
            } else {
                return path.in(list);
            }
        };
    }
}
