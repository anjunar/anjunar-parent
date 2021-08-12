package de.bitvale.anjunar.pages.page.questions.question;

import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import java.util.UUID;

public class QuestionOwnerPredicate {

    private final Identity identity;

    private final EntityManager entityManager;

    public QuestionOwnerPredicate(Identity identity, EntityManager entityManager) {
        this.identity = identity;
        this.entityManager = entityManager;
    }

    public boolean apply(UUID id) {
        if (identity.hasRole("Administrator")) {
            return true;
        }
        Question question = entityManager.find(Question.class, id);
        return question.getOwner().getId().equals(identity.getUser().getId());
    }
}
