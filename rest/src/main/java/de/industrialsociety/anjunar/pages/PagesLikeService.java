package de.industrialsociety.anjunar.pages;

import de.industrialsociety.common.ddd.AbstractCriteriaSearchService;
import de.industrialsociety.common.security.Identity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class PagesLikeService extends AbstractCriteriaSearchService<Page, PagesLikeSearch> {

    @Inject
    public PagesLikeService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public PagesLikeService() {
        super(null, null);
    }

}
