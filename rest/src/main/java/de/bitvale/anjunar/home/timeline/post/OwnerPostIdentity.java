package de.bitvale.anjunar.home.timeline.post;

import de.bitvale.anjunar.timeline.AbstractPost;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import java.util.UUID;

public class OwnerPostIdentity {

    private final Identity identity;

    private final EntityManager entityManager;

    public OwnerPostIdentity(Identity identity, EntityManager entityManager) {
        this.identity = identity;
        this.entityManager = entityManager;
    }

    public boolean apply(UUID id) {
        AbstractPost post = entityManager.find(AbstractPost.class, id);
        if (identity.hasRole("Administrator")) {
            return true;
        }
        return post.getOwner().equals(identity.getUser());
    }
}
