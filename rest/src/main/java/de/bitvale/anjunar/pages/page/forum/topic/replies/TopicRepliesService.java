package de.bitvale.anjunar.pages.page.forum.topic.replies;

import de.bitvale.common.security.Identity;
import de.bitvale.common.ddd.AbstractCriteriaSearchService;
import de.bitvale.anjunar.pages.page.forum.Reply;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class TopicRepliesService extends AbstractCriteriaSearchService<Reply, TopicRepliesSearch> {

    @Inject
    public TopicRepliesService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public TopicRepliesService() {
        this(null, null);
    }

}
