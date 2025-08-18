package com.cariochi.spec.data;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.Type;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.split;

@RequiredArgsConstructor
public final class MetaPathResolver<T, Y> implements PathResolver<T, Y> {

    @Override
    public Path<Y> resolve(Root<T> root, String dottedPath, JoinType joinType) {

        String[] fields = split(dottedPath, '.');
        From<?, ?> from = root;
        Path<?> path = root;
        ManagedType<?> type = root.getModel();


        for (int i = 0; i < fields.length; i++) {

            final From<?, ?> finalFrom = from;

            final String field = fields[i];
            if (isBlank(field)) {
                continue;
            }

            if (type == null && i < fields.length - 1) {
                throw new IllegalArgumentException("Cannot dereference '%s' after non-managed/basic type in path '%s'.".formatted(field, dottedPath));
            }

            final Attribute<?, ?> attr = getAttribute(type, field);

            if (attr instanceof MapAttribute<?, ?, ?> mapAttr) {

                MapJoin<?, ?, ?> join = findJoin(joinType, from, attr)
                        .map(MapJoin.class::cast)
                        .orElseGet(() -> finalFrom.joinMap(field, joinType));

                String next = (i + 1 < fields.length) ? fields[i + 1] : null;

                if ("key".equals(next)) {
                    path = join.key();
                    type = mapAttr.getKeyType() instanceof ManagedType<?> managed ? managed : null;
                    i++;
                } else {
                    path = join.value();
                    type = mapAttr.getElementType() instanceof ManagedType<?> managed ? managed : null;
                    if (type != null) {
                        from = join;
                    }
                    if ("value".equals(next)) {
                        i++;
                    }
                }
                continue;
            }

            switch (attr.getPersistentAttributeType()) {
                case BASIC, EMBEDDED, MANY_TO_ONE, ONE_TO_ONE -> {
                    path = path.get(field);
                }
                case ONE_TO_MANY, MANY_TO_MANY, ELEMENT_COLLECTION -> {
                    from = findJoin(joinType, from, attr)
                            .map(Join.class::cast)
                            .orElseGet(() -> finalFrom.join(field, joinType));
                    path = from;
                }
            }

            type = getManagedType(attr);
        }

        return (Path<Y>) path;
    }

    private static Optional<? extends Join<?, ?>> findJoin(JoinType joinType, From<?, ?> from, Attribute<?, ?> attr) {
        return from.getJoins().stream()
                .filter(join -> join.getAttribute().equals(attr))
                .filter(join -> joinType.equals(join.getJoinType()))
                .findAny();
    }

    private static Attribute<?, ?> getAttribute(ManagedType<?> type, String name) {
        try {
            return type.getAttribute(name);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Attribute '%s' not found on %s".formatted(name, type.getJavaType().getName()), ex);
        }
    }

    private ManagedType<?> getManagedType(Attribute<?, ?> attr) {
        Type<?> type = switch (attr) {
            case SingularAttribute<?, ?> singular -> singular.getType();
            case PluralAttribute<?, ?, ?> plural -> plural.getElementType();
            default -> null;
        };
        return type instanceof ManagedType<?> managed ? managed : null;
    }


}
