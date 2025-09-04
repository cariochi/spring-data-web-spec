package com.cariochi.spec.values;

import org.springframework.core.convert.TypeDescriptor;

import java.util.List;

import static org.springframework.core.convert.TypeDescriptor.collection;

public record SpecValue<V>(Object raw, ValueConverter<V> converter) {

    public V convertTo(Class<?> type) {
        return convertTo(TypeDescriptor.valueOf(type));
    }

    public V convertToCollectionOf(Class<?> elemType) {
        return convertTo(collection(List.class, TypeDescriptor.valueOf(elemType)));
    }

    public V convertTo(TypeDescriptor typeDescriptor) {
        return converter.convert(raw, typeDescriptor);
    }
}
