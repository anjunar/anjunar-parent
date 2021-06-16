package de.industrialsociety.common.rest.api.jaxrs.provider;

import de.industrialsociety.common.ddd.AbstractEntity_;
import de.industrialsociety.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.ddd.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

public class GenericInIdsProvider<E extends AbstractEntity> extends AbstractRestPredicateProvider<List<UUID>, E> {
    @Override
    public Predicate build(List<UUID> value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<E> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.disjunction();
        }
        if (value.isEmpty()) {
            return builder.conjunction();
        }
        return root.get(AbstractEntity_.id).in(value);
    }
}
