package de.bitvale.anjunar.home.timeline.post.comments;

import de.bitvale.anjunar.pages.page.Question_;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.anjunar.timeline.Comment_;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class OwnerProvider extends AbstractRestPredicateProvider<UserSelect, Comment> {
    @Override
    public Predicate build(UserSelect value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Comment> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        return builder.equal(root.get(Comment_.owner).get(User_.id), value);
    }
}
