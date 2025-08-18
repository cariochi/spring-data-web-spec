package com.cariochi.spec.operator;

import com.cariochi.spec.data.SpecPath;
import com.cariochi.spec.data.SpecValue;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public class ContainsIgnoreCase<T> implements BaseOperator<T, String, String> {

    @Override
    public Specification<T> buildSpecification(SpecPath<T, String> specPath, SpecValue<String> specValue) {
        return (root, query, cb) -> {
            Path<String> path = specPath.resolve(root);
            String value = trimToEmpty(specValue.convertTo(String.class));
            return value.isEmpty()
                    ? cb.conjunction()
                    : cb.like(cb.lower(path), lowerCase("%" + value + "%"));
        };
    }
}
