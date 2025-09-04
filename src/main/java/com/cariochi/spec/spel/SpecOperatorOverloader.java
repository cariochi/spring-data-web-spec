package com.cariochi.spec.spel;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Operation;
import org.springframework.expression.OperatorOverloader;
import org.springframework.lang.Nullable;

import static java.util.Objects.isNull;

public final class SpecOperatorOverloader implements OperatorOverloader {

    @Override
    public boolean overridesOperation(Operation op, @Nullable Object left, @Nullable Object right) {
        return switch (op) {
            case MULTIPLY, ADD -> isSpec(left) || isSpec(right);
            case SUBTRACT -> (isSpec(left) && isNull(right)) || (isNull(left) && isSpec(right));
            default -> false;
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object operate(Operation op, @Nullable Object left, @Nullable Object right) throws EvaluationException {
        return switch (op) {
            case MULTIPLY -> and((Specification<Object>) left, (Specification<Object>) right);
            case ADD -> or((Specification<Object>) left, (Specification<Object>) right);
            case SUBTRACT -> {
                if (isSpec(left) && isNull(right)) {
                    yield not((Specification<Object>) left);
                } else if (isNull(left) && isSpec(right)) {
                    yield not((Specification<Object>) right);
                }
                throw new EvaluationException("Invalid operand for NOT operation");
            }
            default -> throw new EvaluationException("Unsupported op: " + op);
        };
    }

    private static boolean isSpec(Object o) {return o == null || o instanceof Specification<?>;}

    private static <T> Specification<T> and(Specification<T> a, Specification<T> b) {
        if (a == null) return b;
        if (b == null) return a;
        return a.and(b);
    }

    private static <T> Specification<T> or(Specification<T> a, Specification<T> b) {
        if (a == null) return b;
        if (b == null) return a;
        return a.or(b);
    }

    private static <T> Specification<T> not(Specification<T> a) {
        return (a == null) ? null : Specification.not(a);
    }
}
