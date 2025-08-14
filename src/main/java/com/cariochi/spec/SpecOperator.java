package com.cariochi.spec;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.jpa.domain.Specification;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.split;
import static org.springframework.core.convert.TypeDescriptor.collection;

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
    Specification<T> buildSpecification(SpecContext<T, Y, V> context);

    /**
     * Context class for holding the path and value function used in building specifications.
     *
     * @param <T> the entity type
     * @param <Y> the type of the path
     * @param <V> the type of the value
     */
    @RequiredArgsConstructor
    class SpecContext<T, Y, V> {


        private final String path;
        private final Function<TypeDescriptor, V> value;

        /**
         * Resolves the entity graph path starting from the given root.
         * <p>
         * Traverses the entity graph using the dot-notation path stored in the context.
         * <p>
         * For example, a path like {@code "customer.address.city"} will navigate from the root entity to the customer field, then to the address field, and finally to
         * the city field.
         *
         * @param root the JPA criteria root representing the entity from which to start the path
         * @return the resolved path of type Y in the entity graph
         */
        @SuppressWarnings("unchecked")
        public Path<Y> path(Root<T> root) {
            final String[] fields = split(path, '.');
            Path<?> path = root;
            for (String f : fields) {
                if (isBlank(f)) {
                    continue;
                }
                path = path.get(f);
            }
            return (Path<Y>) path;
        }

        /**
         * Returns the value based on the provided type.
         *
         * @param type the class type for which to get the value
         * @return the value of type V
         */
        public V valueOf(Class<?> type) {
            return valueOf(TypeDescriptor.valueOf(type));
        }

        /**
         * Returns a collection of values based on the provided element type.
         *
         * @param elemType the class type of the elements in the collection
         * @return a collection of values of type V
         */
        public V collectionOf(Class<?> elemType) {
            return valueOf(collection(List.class, TypeDescriptor.valueOf(elemType)));
        }

        /**
         * Returns the value based on the provided TypeDescriptor.
         *
         * @param typeDescriptor the TypeDescriptor for which to get the value
         * @return the value of type V
         */
        public V valueOf(TypeDescriptor typeDescriptor) {
            return value.apply(typeDescriptor);
        }
    }
}
