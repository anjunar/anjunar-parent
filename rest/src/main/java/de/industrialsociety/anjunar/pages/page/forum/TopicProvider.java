package de.industrialsociety.anjunar.pages.page.forum;

import de.industrialsociety.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.industrialsociety.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TopicProvider extends AbstractRestPredicateProvider<String, Topic> {
    @Override
    public Predicate build(String value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Topic> root, CriteriaQuery<?> query) {
        if (value == null || value.equals("")) {
            return builder.conjunction();
        }
        return builder.gt(builder.function("contains", Integer.class, root.get(Topic_.topic), builder.literal(value), builder.literal(1)), 0);
    }
}
