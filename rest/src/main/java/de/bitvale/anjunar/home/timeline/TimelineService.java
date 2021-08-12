package de.bitvale.anjunar.home.timeline;

import de.bitvale.anjunar.timeline.AbstractPost;
import de.bitvale.common.ddd.AbstractCriteriaSearchService;
import de.bitvale.common.security.Identity;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class TimelineService extends AbstractCriteriaSearchService<AbstractPost, TimelineSearch> {

    @Inject
    public TimelineService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public TimelineService() {
        this(null, null);
    }
}
