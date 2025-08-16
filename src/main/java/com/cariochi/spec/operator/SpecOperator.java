package com.cariochi.spec.operator;

import com.cariochi.spec.data.SpecContext;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface for building JPA specifications based on a context that includes a path and a value function.
 *
 * @param <T> the entity type
 * @param <Y> the type of the path
 * @param <V> the type of the value
 */
public interface SpecOperator<T, Y, V> {

    /**
     * Builds a JPA Specification based on the provided context.
     *
     * @param context the context containing the path and value function
     * @return a Specification for the entity type T
     */
    Specification<T> getSpecification(SpecContext<T, Y, V> context);

}
