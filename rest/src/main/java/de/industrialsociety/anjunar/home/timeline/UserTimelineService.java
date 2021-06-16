package de.industrialsociety.anjunar.home.timeline;

import de.industrialsociety.common.ddd.AbstractCriteriaSearchService;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.anjunar.control.user.timeline.UserPost;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class UserTimelineService extends AbstractCriteriaSearchService<UserPost, UserTimelineSearch> {

    @Inject
    public UserTimelineService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public UserTimelineService() {
        this(null, null);
    }
}
