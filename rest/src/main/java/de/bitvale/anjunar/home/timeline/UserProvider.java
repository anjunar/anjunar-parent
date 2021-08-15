package de.bitvale.anjunar.home.timeline;

import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.pages.page.Question_;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserProvider extends AbstractRestPredicateProvider<UserSelect, Question> {
    @Override
    public Predicate build(UserSelect value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Question> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        return builder.equal(root.get(Question_.owner).get(User_.id), value.getId());
    }
}
