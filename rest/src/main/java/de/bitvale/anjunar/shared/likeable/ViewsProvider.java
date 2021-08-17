package de.bitvale.anjunar.shared.likeable;

import de.bitvale.anjunar.pages.page.Answer;
import de.bitvale.anjunar.pages.page.Answer_;
import de.bitvale.anjunar.pages.page.Question_;
import de.bitvale.anjunar.shared.Likeable_;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ViewsProvider extends AbstractRestPredicateProvider<Integer, Answer> {
    @Override
    public Predicate build(Integer value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Answer> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        return builder.greaterThanOrEqualTo(root.get(Likeable_.views), value);
    }
}
