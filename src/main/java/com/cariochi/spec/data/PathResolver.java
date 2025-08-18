package com.cariochi.spec.data;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

public interface PathResolver<T, Y> {

    Path<Y> resolve(Root<T> root, String dottedPath, JoinType joinType);
}
