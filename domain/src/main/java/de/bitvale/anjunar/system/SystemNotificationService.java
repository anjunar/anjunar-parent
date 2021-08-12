package de.bitvale.anjunar.system;

import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.pages.page.Answer;
import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.timeline.SystemPost;
import de.bitvale.common.mail.Email;
import de.bitvale.common.security.Identity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ResourceBundle;

@ApplicationScoped
public class SystemNotificationService {

    private final Email email;

    private final Identity identity;

    private final EntityManager entityManager;

    @Inject
    public SystemNotificationService(Email email, Identity identity, EntityManager entityManager) {
        this.email = email;
        this.identity = identity;
        this.entityManager = entityManager;
    }

    public SystemNotificationService() {
        this(null, null, null);
    }

    public void notifyOnPageSave(Page page) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("de.bitvale.anjunar.i18nMessages", identity.getLanguage());

        SystemPost systemPost = new SystemPost();
        systemPost.setOwner(identity.getUser());
        systemPost.setText(resourceBundle.getString("de.bitvale.anjunar.system.SystemNotificationService.notifyOnPageSave") + page.getTitle());
        systemPost.setHash("#/anjunar/pages/page?id=" + page.getId());
        entityManager.persist(systemPost);
    }

    public void notifyOnPageUpdate(Page page) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("de.bitvale.anjunar.i18nMessages", identity.getLanguage());

        SystemPost systemPost = new SystemPost();
        systemPost.setOwner(identity.getUser());
        systemPost.setText(resourceBundle.getString("de.bitvale.anjunar.system.SystemNotificationService.notifyOnPageUpdate") + page.getTitle());
        systemPost.setHash("#/anjunar/pages/page?id=" + page.getId());
        entityManager.persist(systemPost);
    }

    public void notifyOnQuestionUpdate(Question question) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("de.bitvale.anjunar.i18nMessages", identity.getLanguage());

        SystemPost systemPost = new SystemPost();
        systemPost.setOwner(identity.getUser());
        systemPost.setText(resourceBundle.getString("de.bitvale.anjunar.system.SystemNotificationService.notifyOnQuestionUpdate") + question.getTopic());
        systemPost.setHash("#/anjunar/pages/page/question/replies?id=" + question.getId());
        entityManager.persist(systemPost);
    }

    public void notifyOnQuestionSave(Question question) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("de.bitvale.anjunar.i18nMessages", identity.getLanguage());

        SystemPost systemPost = new SystemPost();
        systemPost.setOwner(identity.getUser());
        systemPost.setText(resourceBundle.getString("de.bitvale.anjunar.system.SystemNotificationService.notifyOnQuestionSave") + question.getTopic());
        systemPost.setHash("#/anjunar/pages/page/question/replies?id=" + question.getId());
        entityManager.persist(systemPost);
    }

    public void notifyOnAnswerUpdate(Answer answer) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("de.bitvale.anjunar.i18nMessages", identity.getLanguage());

        SystemPost systemPost = new SystemPost();
        systemPost.setOwner(identity.getUser());
        systemPost.setText(resourceBundle.getString("de.bitvale.anjunar.system.SystemNotificationService.notifyOnAnswerUpdate") + answer.getTopic().getTopic());
        systemPost.setHash("#/anjunar/pages/page/question/replies?id=" + answer.getTopic().getId());
        entityManager.persist(systemPost);
    }

    public void notifyOnAnswerSave(Answer answer) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("de.bitvale.anjunar.i18nMessages", identity.getLanguage());

        SystemPost systemPost = new SystemPost();
        systemPost.setOwner(identity.getUser());
        systemPost.setText(resourceBundle.getString("de.bitvale.anjunar.system.SystemNotificationService.notifyOnAnswerSave") + answer.getTopic().getTopic());
        systemPost.setHash("#/anjunar/pages/page/question/replies?id=" + answer.getTopic().getId());
        entityManager.persist(systemPost);
    }

}
