package de.industrialsociety.anjunar.home.timeline;

import de.industrialsociety.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.security.User_;
import de.industrialsociety.anjunar.control.user.timeline.UserPost;
import de.industrialsociety.anjunar.control.user.timeline.UserPost_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class UserPostProvider extends AbstractRestPredicateProvider<UUID, UserPost> {
    @Override
    public Predicate build(UUID value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<UserPost> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        return builder.equal(root.get(UserPost_.user).get(User_.id), value);
    }
}
