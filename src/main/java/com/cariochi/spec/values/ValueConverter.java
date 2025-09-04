package com.cariochi.spec.values;

import org.springframework.core.convert.TypeDescriptor;

public interface ValueConverter<V> {

    V convert(Object raw, TypeDescriptor type);
}
