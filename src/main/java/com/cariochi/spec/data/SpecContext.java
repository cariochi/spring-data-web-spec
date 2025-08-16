package com.cariochi.spec.data;

public record SpecContext<T, Y, V>(SpecPath<T, Y> path, SpecValue<V> value, boolean distinct) {
}
