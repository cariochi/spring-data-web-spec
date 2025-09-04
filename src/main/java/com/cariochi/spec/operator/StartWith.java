package com.cariochi.spec.operator;

import com.cariochi.spec.attributes.SpecAttribute;
import com.cariochi.spec.values.SpecValue;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public class StartWith<T> implements BaseOperator<T, String, String> {

    @Override
    public Specification<T> buildSpecification(SpecAttribute<T, String> attribute, SpecValue<String> specValue) {
        return (root, query, cb) -> {
            Path<String> path = attribute.resolve(root);
            String value = trimToEmpty(specValue.convertTo(String.class));
            return value.isEmpty()
                    ? cb.conjunction()
                    : cb.like(path, value + "%");
        };
    }
}
