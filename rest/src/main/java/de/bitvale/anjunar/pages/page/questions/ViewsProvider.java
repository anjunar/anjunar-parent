package de.bitvale.anjunar.pages.page.questions;

import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.pages.page.Question_;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ViewsProvider extends AbstractRestPredicateProvider<Integer, Question> {
    @Override
    public Predicate build(Integer value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Question> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        return builder.greaterThanOrEqualTo(root.get(Question_.views), value);
    }
}
