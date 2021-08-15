package de.bitvale.anjunar.home.timeline.post.comments;

import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.anjunar.timeline.Comment_;
import de.bitvale.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TextProvider extends AbstractRestPredicateProvider<String, Comment> {
    @Override
    public Predicate build(String value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<Comment> root, CriteriaQuery<?> query) {
        if (value == null || value.equals("")) {
            return builder.conjunction();
        }
        return builder.gt(builder.function("contains", Integer.class, root.get(Comment_.text), builder.literal(value), builder.literal(1)), 0);
    }
}
