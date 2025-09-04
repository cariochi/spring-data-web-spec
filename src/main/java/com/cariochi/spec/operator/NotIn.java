package com.cariochi.spec.operator;

import com.cariochi.spec.attributes.SpecAttribute;
import com.cariochi.spec.values.SpecValue;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class NotIn<T, Y> implements BaseOperator<T, Y, List<Y>> {

    @Override
    public Specification<T> buildSpecification(SpecAttribute<T, Y> attribute, SpecValue<List<Y>> specValue) {
        return (root, query, cb) -> {
            Path<Y> path = attribute.resolve(root);
            List<Y> list = specValue.convertToCollectionOf(path.getJavaType());
            if (list.isEmpty()) {
                return cb.conjunction();
            } else if (list.size() == 1) {
                return cb.notEqual(path, list.getFirst());
            } else {
                return path.in(list).not();
            }
        };
    }
}
