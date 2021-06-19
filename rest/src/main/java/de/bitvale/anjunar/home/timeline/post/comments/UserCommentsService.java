package de.bitvale.anjunar.home.timeline.post.comments;

import de.bitvale.common.ddd.AbstractCriteriaSearchService;
import de.bitvale.common.security.Identity;
import de.bitvale.anjunar.timeline.Comment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class UserCommentsService extends AbstractCriteriaSearchService<Comment, UserCommentsSearch> {

    @Inject
    public UserCommentsService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public UserCommentsService() {
        this(null, null);
    }

}
