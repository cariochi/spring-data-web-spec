package com.cariochi.spec.spel;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

import static com.cariochi.spec.spel.SpecInfixRewriter.toArithmetic;

public final class SpecSpelEvaluator {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    @SuppressWarnings("unchecked")
    public static <T> Specification<T> evaluate(String expression, Map<String, Specification<T>> atoms, boolean strict) {
        if (expression == null || expression.isBlank()) return null;
        var ctx = new StandardEvaluationContext(atoms);
        ctx.setOperatorOverloader(new SpecOperatorOverloader());
        ctx.addPropertyAccessor(new MapAccessor(strict));
        Expression expr = PARSER.parseExpression(toArithmetic(expression));
        Object out = expr.getValue(ctx);
        return (Specification<T>) out;
    }

}
