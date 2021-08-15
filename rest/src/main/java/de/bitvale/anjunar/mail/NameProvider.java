package de.bitvale.anjunar.mail;

import com.google.common.base.Strings;
import de.bitvale.common.mail.Template;
import de.bitvale.common.mail.Template_;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NameProvider extends AbstractRestPredicateProvider<String, Template> {
    @Override
    public Predicate build(String value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Template> root, CriteriaQuery<?> query) {
        if (Strings.isNullOrEmpty(value)) {
            return builder.conjunction();
        }
        return builder.like(builder.lower(root.get(Template_.name)), value.toLowerCase() + "%");
    }
}
