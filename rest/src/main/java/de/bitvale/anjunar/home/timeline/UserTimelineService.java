package de.bitvale.anjunar.home.timeline;

import de.bitvale.common.ddd.AbstractCriteriaSearchService;
import de.bitvale.common.security.Identity;
import de.bitvale.anjunar.control.user.timeline.UserPost;

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
