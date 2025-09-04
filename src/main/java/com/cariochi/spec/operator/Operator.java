package com.cariochi.spec.operator;

import com.cariochi.spec.SpecContext;
import org.springframework.data.jpa.domain.Specification;

public interface Operator<T, Y, V> {

    Specification<T> getSpecification(SpecContext<T, Y, V> context);

}
