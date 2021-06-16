package de.industrialsociety.anjunar.pages.page.images;

import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.ddd.AbstractCriteriaSearchService;
import de.industrialsociety.anjunar.pages.PageImage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class ImagesService extends AbstractCriteriaSearchService<PageImage, ImagesSearch> {

    @Inject
    public ImagesService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public ImagesService() {
        this(null, null);
    }

}
