package com.cariochi.spec;

import com.cariochi.spec.attributes.SpecAttribute;
import com.cariochi.spec.values.SpecValue;

public record SpecContext<T, Y, V>(SpecAttribute<T, Y> attribute, SpecValue<V> specValue, boolean distinct) {
}
