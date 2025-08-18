package com.cariochi.spec.data;

import org.springframework.core.convert.TypeDescriptor;

public interface ValueConverter<V> {

    V convert(Object raw, TypeDescriptor type);
}
