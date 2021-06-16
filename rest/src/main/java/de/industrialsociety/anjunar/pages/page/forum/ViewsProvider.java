package de.industrialsociety.anjunar.pages.page.forum;

import de.industrialsociety.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.industrialsociety.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ViewsProvider extends AbstractRestPredicateProvider<Integer, Topic> {
    @Override
    public Predicate build(Integer value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Topic> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        return builder.greaterThan(root.get(Topic_.views), value);
    }
}
