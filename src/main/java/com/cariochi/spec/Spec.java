package com.cariochi.spec;

import com.cariochi.spec.operator.Equal;
import com.cariochi.spec.operator.SpecOperator;
import jakarta.persistence.criteria.JoinType;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.function.Supplier;

import static jakarta.persistence.criteria.JoinType.INNER;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface Spec {

    /**
     * Annotation to bind a method parameter to a URI template variable.
     * <p>
     * Used for extracting values from the path of a request.
     */
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Documented
    @Repeatable(PathVariable.Many.class)
    @interface PathVariable {

        /**
         * The name of the path variable to bind to.
         */
        String name();

        /**
         * The path expression to extract the variable from.
         */
        String path() default "";

        /**
         * The operator class to use for comparison. Defaults to {@link Equal}.
         */
        Class<? extends SpecOperator> operator() default Equal.class;

        /**
         * Whether the path variable is required. Defaults to {@code false}.
         */
        boolean required() default false;

        boolean distinct() default false;

        JoinType joinType() default INNER;

        @Target(PARAMETER)
        @Retention(RUNTIME)
        @Documented
        @interface Many {

            PathVariable[] value();

        }
    }

    /**
     * Annotation to bind a method parameter to a request header.
     * <p>
     * Used for extracting values from the headers of a request.
     */
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Documented
    @Repeatable(RequestHeader.Many.class)
    @interface RequestHeader {

        /**
         * The name of the request header to bind to.
         */
        String name();

        /**
         * The path expression to extract the header from.
         */
        String path() default "";

        /**
         * The operator class to use for comparison. Defaults to {@link Equal}.
         */
        Class<? extends SpecOperator> operator() default Equal.class;

        /**
         * Whether the request header is required. Defaults to {@code false}.
         */
        boolean required() default false;

        boolean distinct() default false;

        JoinType joinType() default INNER;

        @Target(PARAMETER)
        @Retention(RUNTIME)
        @Documented
        @interface Many {

            RequestHeader[] value();

        }
    }

    /**
     * Annotation to bind a method parameter to a request parameter.
     * <p>
     * Used for extracting values from the query parameters of a request.
     */
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Documented
    @Repeatable(RequestParam.Many.class)
    @interface RequestParam {

        /**
         * The name of the request parameter to bind to.
         */
        String name();

        /**
         * The path expression to extract the parameter from.
         */
        String path() default "";

        /**
         * The operator class to use for comparison. Defaults to {@link Equal}.
         */
        Class<? extends SpecOperator> operator() default Equal.class;

        /**
         * Whether the request parameter is required. Defaults to {@code false}.
         */
        boolean required() default false;

        boolean distinct() default false;

        JoinType joinType() default INNER;

        @Target(PARAMETER)
        @Retention(RUNTIME)
        @Documented
        @interface Many {

            RequestParam[] value();

        }
    }


    /**
     * Annotation to bind a method parameter to an access control rule.
     * <p>
     * Used for specifying access control logic based on a dynamic value supplied at runtime.
     * </p>
     */
    @Target(PARAMETER)
    @Retention(RUNTIME)
    @Documented
    @Repeatable(AccessControl.Many.class)
    @interface AccessControl {

        /**
         * The path expression to extract the value for access control.
         */
        String path();

        /**
         * The supplier class that provides the value to compare against.
         */
        Class<? extends Supplier<?>> valueSupplier();

        /**
         * The operator class to use for comparison.
         */
        Class<? extends SpecOperator> operator();

        /**
         * Whether the access control is required. Defaults to {@code false}.
         */
        boolean required() default false;

        boolean distinct() default false;

        JoinType joinType() default INNER;

        @Target(PARAMETER)
        @Retention(RUNTIME)
        @Documented
        @interface Many {

            AccessControl[] value();

        }
    }

}
