package de.bitvale.anjunar.control.users;

import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import de.bitvale.common.security.User_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BirthDataProvider extends AbstractRestPredicateProvider<BirthDateForm, User> {
    @Override
    public Predicate build(BirthDateForm value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<User> root, CriteriaQuery<?> query) {
        if (value != null && value.getStart() != null && value.getEnd() != null) {
            return builder.between(root.get(User_.birthDate), value.getStart(), value.getEnd());
        }
        if (value != null && value.getStart() != null && value.getEnd() == null) {
            return builder.greaterThan(root.get(User_.birthDate), value.getStart());
        }
        if (value != null && value.getStart() == null && value.getEnd() != null) {
            return builder.lessThan(root.get(User_.birthDate), value.getEnd());
        }
        return builder.conjunction();
    }
}
