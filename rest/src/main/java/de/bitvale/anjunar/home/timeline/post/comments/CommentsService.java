package de.bitvale.anjunar.home.timeline.post.comments;

import de.bitvale.common.ddd.AbstractCriteriaSearchService;
import de.bitvale.common.security.Identity;
import de.bitvale.anjunar.timeline.Comment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class CommentsService extends AbstractCriteriaSearchService<Comment, CommentsSearch> {

    @Inject
    public CommentsService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public CommentsService() {
        this(null, null);
    }

}
