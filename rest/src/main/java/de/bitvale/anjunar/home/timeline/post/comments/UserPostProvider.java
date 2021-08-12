package de.bitvale.anjunar.home.timeline.post.comments;

import de.bitvale.anjunar.timeline.AbstractPost_;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;
import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.anjunar.timeline.Comment_;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class UserPostProvider extends AbstractRestPredicateProvider<UUID, Comment> {
    @Override
    public Predicate build(UUID value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Comment> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }

        return builder.equal(root.get(Comment_.post).get(AbstractPost_.id), value);
    }
}
