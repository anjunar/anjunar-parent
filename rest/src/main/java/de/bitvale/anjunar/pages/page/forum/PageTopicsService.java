package de.bitvale.anjunar.pages.page.forum;

import de.bitvale.common.security.Identity;
import de.bitvale.common.ddd.AbstractCriteriaSearchService;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class PageTopicsService extends AbstractCriteriaSearchService<Topic, PageTopicsSearch> {

    @Inject
    public PageTopicsService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public PageTopicsService() {
        this(null, null);
    }

}
