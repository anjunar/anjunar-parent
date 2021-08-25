package de.bitvale.anjunar.home.timeline;

import com.google.common.base.Strings;
import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.pages.page.Question_;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TextProvider extends AbstractRestPredicateProvider<String, Question> {
    @Override
    public Predicate build(String value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Question> root, CriteriaQuery<?> query) {
        if (Strings.isNullOrEmpty(value)) {
            return builder.conjunction();
        }
        return builder.greaterThan(builder.function("contains", Integer.class, root.get(Question_.text), builder.literal("about(" + value + ")")), 0);
    }
}
