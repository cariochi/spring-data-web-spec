package com.cariochi.spec.spel;

import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

import java.util.Map;

@RequiredArgsConstructor
public final class MapAccessor implements PropertyAccessor {

    private final boolean strict;

    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return new Class[]{Map.class};
    }

    @Override
    public boolean canRead(EvaluationContext context, Object target, String name) {
        return target instanceof Map;
    }

    @Override
    public TypedValue read(EvaluationContext context, Object target, String name) {
        Map<?, ?> map = (Map<?, ?>) target;
        if (strict && !map.containsKey(name)) {
            throw new IllegalArgumentException("Missing specification: " + name);
        }
        return new TypedValue(map.get(name));
    }

    @Override
    public boolean canWrite(EvaluationContext context, Object target, String name) {return false;}

    @Override
    public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
        throw new AccessException("Write not supported");
    }
}
