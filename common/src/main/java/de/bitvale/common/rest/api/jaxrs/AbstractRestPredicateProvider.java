package de.bitvale.common.rest.api.jaxrs;

import de.bitvale.common.security.Identity;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

public abstract class AbstractRestPredicateProvider<V, E> {

    public abstract Predicate build(V value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<E> root, CriteriaQuery<?> query);

    protected Path cursor(Path<?> path, String pathString) {
        if (StringUtils.isEmpty(pathString)) {
            return path;
        }
        String[] paths = pathString.split("\\.");
        Path<?> cursor = path;
        for (String segment : paths) {
            cursor = cursor.get(segment);
        }
        return cursor;
    }

}
