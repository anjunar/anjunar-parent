package de.bitvale.common.rest.api;

import de.bitvale.common.ddd.AbstractEntity;
import de.bitvale.common.ddd.AbstractEntity_;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DurationCreatedProvider extends AbstractRestPredicateProvider<Duration, AbstractEntity> {
    @Override
    public Predicate build(Duration value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<AbstractEntity> root, CriteriaQuery<?> query) {
        if (value == null || value.getStart() == null || value.getEnd() == null) {
            return builder.conjunction();
        }
        LocalDateTime start = value.getStart();
        LocalDateTime end = value.getEnd();
        return builder.between(root.get(AbstractEntity_.created), start.toInstant(ZoneOffset.UTC), end.toInstant(ZoneOffset.UTC));
    }
}
