package de.bitvale.anjunar.pages.page.questions.question.answers.answer;

import de.bitvale.anjunar.pages.page.Answer;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import java.util.UUID;

public class AnswerOwnerPredicate {

    private final Identity identity;

    private final EntityManager entityManager;

    public AnswerOwnerPredicate(Identity identity, EntityManager entityManager) {
        this.identity = identity;
        this.entityManager = entityManager;
    }

    public boolean apply(UUID id) {
        if (identity.hasRole("Administrator")) {
            return true;
        }
        Answer question = entityManager.find(Answer.class, id);
        return question.getOwner().getId().equals(identity.getUser().getId());
    }

}
