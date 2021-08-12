package de.bitvale.anjunar.pages.page.questions.question;

import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import de.bitvale.common.validators.Dom;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class QuestionForm extends AbstractRestEntity {

    @Input(type = "text")
    private UUID page;

    @Input(type = "text")
    private String topic;

    @Input(type = "editor")
    @Dom
    private Editor editor = new Editor();

    @Input(type = "lazyselect")
    private UserSelect owner;

    @Input(type = "number")
    private int views;

    @Input(type = "datetime-local")
    private LocalDateTime created;

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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Set<UserSelect> getLikes() {
        return likes;
    }

    public static QuestionForm factory(Question question) {
        QuestionForm resource = new QuestionForm();

        resource.setId(question.getId());
        resource.setCreated(LocalDateTime.ofInstant(question.getCreated(), ZoneId.systemDefault()));
        resource.setTopic(question.getTopic());
        resource.setEditor(Editor.factory(question.getHtml(), question.getText()));
        resource.setPage(question.getPage().getId());
        resource.setOwner(UserSelect.factory(question.getOwner()));
        resource.setViews(question.getViews());

        for (User like : question.getLikes()) {
            resource.getLikes().add(UserSelect.factory(like));
        }

        return resource;
    }

    public static Question updater(QuestionForm resource, Question question, Identity identity, EntityManager entityManager) {
        question.setTopic(resource.getTopic());
        question.setPage(entityManager.find(Page.class, resource.getPage()));
        question.setHtml(resource.getEditor().getHtml());
        question.setText(resource.getEditor().getText());
        question.setViews(resource.getViews());
        User owner = entityManager.find(User.class, resource.getOwner().getId());
        question.setOwner(owner);
        question.getLikes().clear();
        for (UserSelect like : resource.getLikes()) {
            question.getLikes().add(entityManager.find(User.class, like.getId()));
        }
        return question;
    }
}
