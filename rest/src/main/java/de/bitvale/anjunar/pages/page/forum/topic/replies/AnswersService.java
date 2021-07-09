package de.bitvale.anjunar.pages.page.forum.topic.replies;

import de.bitvale.common.security.Identity;
import de.bitvale.common.ddd.AbstractCriteriaSearchService;
import de.bitvale.anjunar.pages.page.forum.Answer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class AnswersService extends AbstractCriteriaSearchService<Answer, AnswersSearch> {

    @Inject
    public AnswersService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public AnswersService() {
        this(null, null);
    }

}
