package de.industrialsociety.anjunar.pages;

import de.industrialsociety.common.ddd.AbstractJPQLSearchService;
import de.industrialsociety.common.security.Identity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class PagesWordService extends AbstractJPQLSearchService<Page, PagesWordSearch> {

    @Inject
    public PagesWordService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public PagesWordService() {
        super(null, null);
    }

    @Override
    public List<Page> find(PagesWordSearch search) {
        return entityManager.createQuery("select e from Page e where contains(e.text, :word) > 0", getEntityClass())
                .setParameter("word", search.getTitle())
                .setFirstResult(search.getIndex())
                .setMaxResults(search.getLimit())
                .getResultList();
    }

    @Override
    public long count(PagesWordSearch search) {
        return entityManager.createQuery("select count(e) from Page e where contains(e.text, :word) > 0", Long.class)
                .setParameter("word", search.getTitle())
                .getSingleResult();
    }
}
