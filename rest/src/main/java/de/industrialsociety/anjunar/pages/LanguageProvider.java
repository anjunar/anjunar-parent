package de.industrialsociety.anjunar.pages;

import de.industrialsociety.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.industrialsociety.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Locale;

public class LanguageProvider extends AbstractRestPredicateProvider<Locale, Page> {
    @Override
    public Predicate build(Locale value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Page> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        return builder.equal(root.get(Page_.language), value);
    }
}
