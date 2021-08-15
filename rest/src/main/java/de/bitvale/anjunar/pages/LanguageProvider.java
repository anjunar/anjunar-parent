package de.bitvale.anjunar.pages;

import de.bitvale.anjunar.shared.system.Language;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Locale;

public class LanguageProvider extends AbstractRestPredicateProvider<Language, Page> {
    @Override
    public Predicate build(Language value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Page> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        return builder.equal(root.get(Page_.language), value.getLocale());
    }
}
