package com.cariochi.spec.spel;

import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class SpecMap<T> extends HashMap<String, Specification<T>> {

    private final Set<String> usedKeys = new HashSet<>();

    @Override
    public Specification<T> get(Object key) {
        usedKeys.add((String) key);
        return super.get(key);
    }

    public Map<String, Specification<T>> getUnused() {
        return entrySet().stream()
                .filter(e -> !usedKeys.contains(e.getKey()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
