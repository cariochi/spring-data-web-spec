package com.cariochi.spec.operator;

import com.cariochi.spec.data.SpecPath;
import com.cariochi.spec.data.SpecValue;
import jakarta.persistence.criteria.Path;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class In<T, Y> implements BaseOperator<T, Y, List<Y>> {

    @Override
    public Specification<T> buildSpecification(SpecPath<T, Y> specPath, SpecValue<List<Y>> specValue) {
        return (root, query, cb) -> {
            Path<Y> path = specPath.resolve(root);
            List<Y> list = specValue.convertToCollectionOf(path.getJavaType());
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
