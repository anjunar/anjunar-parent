package de.bitvale.anjunar.pages.page.questions.question;

import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.shared.likeable.AbstractLikeableRestEntity;
import de.bitvale.anjunar.shared.likeable.AbstractLikeableRestEntityConverter;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import de.bitvale.common.validators.Dom;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class QuestionForm extends AbstractLikeableRestEntity {

    @Input(type = "text")
    private UUID page;

    @Input(type = "text")
    private String topic;

    @Input(type = "editor")
    @Dom
    private Editor editor = new Editor();

    @Input(type = "lazyselect")
    private UserSelect owner;

    @Input(type = "lazymultiselect")
    private Set<UserSelect> likes = new HashSet<>();

    public UUID getPage() {
        return page;
    }

    public void setPage(UUID page) {
        this.page = page;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public UserSelect getOwner() {
        return owner;
    }

    public void setOwner(UserSelect owner) {
        this.owner = owner;
    }

    public Set<UserSelect> getLikes() {
        return likes;
    }

    private static class QuestionFormConverter extends AbstractLikeableRestEntityConverter<Question, QuestionForm> {

        public static QuestionFormConverter INSTANCE = new QuestionFormConverter();

        public QuestionForm factory(QuestionForm resource, Question question) {
            resource.setId(question.getId());
            resource.setTopic(question.getTopic());
            resource.setEditor(Editor.factory(question.getHtml(), question.getText()));
            resource.setPage(question.getPage().getId());
            resource.setOwner(UserSelect.factory(question.getOwner()));
            return super.factory(resource, question);
        }

        public Question updater(QuestionForm resource, Question question, EntityManager entityManager, Identity identity) {
            question.setTopic(resource.getTopic());
            question.setPage(entityManager.find(Page.class, resource.getPage()));
            question.setHtml(resource.getEditor().getHtml());
            question.setText(resource.getEditor().getText());
            User owner = entityManager.find(User.class, resource.getOwner().getId());
            question.setOwner(owner);
            return super.updater(resource, question, entityManager, identity);
        }
    }

    public static QuestionForm factory(Question question) {
        return QuestionFormConverter.INSTANCE.factory(new QuestionForm(), question);
    }

    public static Question updater(QuestionForm resource, Question question, Identity identity, EntityManager entityManager) {
        return QuestionFormConverter.INSTANCE.updater(resource, question, entityManager, identity);
    }
}
