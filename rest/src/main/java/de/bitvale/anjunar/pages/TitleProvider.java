package de.bitvale.anjunar.pages;

import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TitleProvider extends AbstractRestPredicateProvider<String, Page> {
    @Override
    public Predicate build(String value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Page> root, CriteriaQuery<?> query) {
        if (value == null || value.equals("")) {
            return builder.conjunction();
        }
        return builder.or(
                builder.like(builder.lower(root.get(Page_.title)), value.toLowerCase() + "%"),
                builder.lt(builder.function("levenshtein", Integer.class, root.get(Page_.title), builder.literal(value)), 3)
        );
    }
}
