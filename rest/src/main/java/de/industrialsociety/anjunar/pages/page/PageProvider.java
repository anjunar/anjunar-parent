package de.industrialsociety.anjunar.pages.page;

import de.industrialsociety.anjunar.pages.PageImage;
import de.industrialsociety.anjunar.pages.PageImage_;
import de.industrialsociety.anjunar.pages.Page_;
import de.industrialsociety.common.rest.api.jaxrs.AbstractRestPredicateProvider;
import de.industrialsociety.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class PageProvider extends AbstractRestPredicateProvider<UUID, PageImage> {
    @Override
    public Predicate build(UUID value, Identity identity, EntityManager entityManager, CriteriaBuilder builder, Root<PageImage> root, CriteriaQuery<?> query) {
        if (value == null) {
            return builder.conjunction();
        }
        return builder.equal(root.get(PageImage_.page).get(Page_.id), value);
    }
}

