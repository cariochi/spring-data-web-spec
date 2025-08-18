package com.cariochi.spec.data;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

public record SpecPath<T, Y>(String path, JoinType joinType, PathResolver<T, Y> pathResolver) {

    public Path<Y> resolve(Root<T> root) {
        return pathResolver.resolve(root, path, joinType);
    }
}
