package de.bitvale.anjunar.home.timeline.post.comments.comment;

import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import java.util.UUID;

public class OwnerCommentIdentity {

    private final Identity identity;

    private final EntityManager entityManager;

    public OwnerCommentIdentity(Identity identity, EntityManager entityManager) {
        this.identity = identity;
        this.entityManager = entityManager;
    }

    public boolean apply(UUID id) {
        Comment comment = entityManager.find(Comment.class, id);

        if (identity.hasRole("Administrator")) {
            return true;
        }

        return comment.getOwner().equals(identity.getUser());
    }
}
