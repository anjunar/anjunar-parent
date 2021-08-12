package de.bitvale.anjunar.mail;

import de.bitvale.common.ddd.AbstractCriteriaSearchService;
import de.bitvale.common.mail.Template;
import de.bitvale.common.security.Identity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class TemplatesService extends AbstractCriteriaSearchService<Template, TemplatesSearch> {

    @Inject
    public TemplatesService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public TemplatesService() {
        this(null, null);
    }

}
