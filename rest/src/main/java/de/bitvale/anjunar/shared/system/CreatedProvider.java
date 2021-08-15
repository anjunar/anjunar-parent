package de.bitvale.anjunar.shared.system;

import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.pages.page.Question_;
import de.bitvale.anjunar.shared.system.CreatedForm;
import de.bitvale.common.ddd.AbstractAggregate;
import de.bitvale.common.ddd.AbstractAggregate_;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CreatedProvider extends AbstractRestPredicateProvider<CreatedForm, AbstractAggregate> {
    @Override
    public Predicate build(CreatedForm value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<AbstractAggregate> root, CriteriaQuery<?> query) {
        if (value == null || value.getStart() == null || value.getEnd() == null) {
            return builder.conjunction();
        }
        LocalDateTime start = value.getStart();
        LocalDateTime end = value.getEnd();
        return builder.between(root.get(AbstractAggregate_.created), start.toInstant(ZoneOffset.UTC), end.toInstant(ZoneOffset.UTC));
    }
}
