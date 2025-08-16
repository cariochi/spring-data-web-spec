package com.cariochi.spec.data;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.util.function.BiFunction;

public record SpecPath<T, Y>(String path, BiFunction<Root<T>, String, Path<Y>> pathResolver) {

    public Path<Y> resolve(Root<T> root) {
        return pathResolver.apply(root, path);
    }
}
