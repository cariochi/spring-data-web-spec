package com.cariochi.spec;

import com.cariochi.spec.operator.Equal;
import com.cariochi.spec.operator.Operator;
import com.cariochi.spec.values.HeaderValueResolver;
import com.cariochi.spec.values.ParamValueResolver;
import com.cariochi.spec.values.PathValueResolver;
import jakarta.persistence.criteria.JoinType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.Function;

import static jakarta.persistence.criteria.JoinType.INNER;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface Spec {

    @Target({TYPE, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @Repeatable(Condition.Many.class)
    @interface Condition {

        String name() default "";

        String attribute() default "";

        Class<? extends Operator> operator() default Equal.class;

        boolean required() default false;

        boolean distinct() default false;

        JoinType joinType() default INNER;

        Class<? extends Function<String, ?>> valueResolver() default ParamValueResolver.class;

        @Target(PARAMETER)
        @Retention(RUNTIME)
        @Documented
        @interface Many {
            Condition[] value();
        }
    }

    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Documented
    @interface Expression {
        String value();

        boolean strict() default false;
    }

    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Documented
    @Repeatable(Param.Many.class)
    @Condition(valueResolver = ParamValueResolver.class)
    @interface Param {

        @AliasFor(annotation = Condition.class, attribute = "name")
        String name();

        @AliasFor(annotation = Condition.class, attribute = "attribute")
        String attribute() default "";

        @AliasFor(annotation = Condition.class, attribute = "operator")
        Class<? extends Operator> operator() default Equal.class;

        @AliasFor(annotation = Condition.class, attribute = "required")
        boolean required() default false;

        @AliasFor(annotation = Condition.class, attribute = "distinct")
        boolean distinct() default false;

        @AliasFor(annotation = Condition.class, attribute = "joinType")
        JoinType joinType() default INNER;

        @Target(PARAMETER)
        @Retention(RUNTIME)
        @Documented
        @interface Many {
            Param[] value();
        }
    }

    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Documented
    @Repeatable(Path.Many.class)
    @Condition(valueResolver = PathValueResolver.class)
    @interface Path {

        @AliasFor(annotation = Condition.class, attribute = "name")
        String name();

        @AliasFor(annotation = Condition.class, attribute = "attribute")
        String attribute() default "";

        @AliasFor(annotation = Condition.class, attribute = "operator")
        Class<? extends Operator> operator() default Equal.class;

        @AliasFor(annotation = Condition.class, attribute = "required")
        boolean required() default false;

        @AliasFor(annotation = Condition.class, attribute = "distinct")
        boolean distinct() default false;

        @AliasFor(annotation = Condition.class, attribute = "joinType")
        JoinType joinType() default INNER;

        @Target(PARAMETER)
        @Retention(RUNTIME)
        @Documented
        @interface Many {
            Path[] value();
        }
    }

    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Documented
    @Repeatable(Header.Many.class)
    @Condition(valueResolver = HeaderValueResolver.class)
    @interface Header {

        @AliasFor(annotation = Condition.class, attribute = "name")
        String name();

        @AliasFor(annotation = Condition.class, attribute = "attribute")
        String attribute() default "";

        @AliasFor(annotation = Condition.class, attribute = "operator")
        Class<? extends Operator> operator() default Equal.class;

        @AliasFor(annotation = Condition.class, attribute = "required")
        boolean required() default false;

        @AliasFor(annotation = Condition.class, attribute = "distinct")
        boolean distinct() default false;

        @AliasFor(annotation = Condition.class, attribute = "joinType")
        JoinType joinType() default INNER;

        @Target(PARAMETER)
        @Retention(RUNTIME)
        @Documented
        @interface Many {
            Header[] value();
        }
    }
}
