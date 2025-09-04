package com.cariochi.spec.attributes;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

public record SpecAttribute<T, Y>(String attribute, JoinType joinType, AttributeResolver<T, Y> attributeResolver) {

    public Path<Y> resolve(Root<T> root) {
        return attributeResolver.resolve(root, attribute, joinType);
    }
}
