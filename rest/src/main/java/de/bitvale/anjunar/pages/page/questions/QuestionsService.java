package de.bitvale.anjunar.pages.page.questions;

import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.common.security.Identity;
import de.bitvale.common.ddd.AbstractCriteriaSearchService;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class QuestionsService extends AbstractCriteriaSearchService<Question, QuestionsSearch> {

    @Inject
    public QuestionsService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public QuestionsService() {
        this(null, null);
    }

}
