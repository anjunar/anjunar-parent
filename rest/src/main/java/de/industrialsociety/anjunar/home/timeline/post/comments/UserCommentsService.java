package de.industrialsociety.anjunar.home.timeline.post.comments;

import de.industrialsociety.common.ddd.AbstractCriteriaSearchService;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.anjunar.timeline.Comment;

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
