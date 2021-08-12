package de.bitvale.anjunar.pages;

import de.bitvale.common.ddd.AbstractCriteriaSearchService;
import de.bitvale.common.security.Identity;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class PagesService extends AbstractCriteriaSearchService<Page, PagesSearch> {

    @Inject
    public PagesService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public PagesService() {
        this(null, null);
    }

}
