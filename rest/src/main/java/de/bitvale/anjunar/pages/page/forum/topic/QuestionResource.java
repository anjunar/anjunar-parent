package de.bitvale.anjunar.pages.page.forum.topic;

import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.pages.page.forum.Question;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.validators.Dom;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class QuestionResource extends AbstractRestEntity<QuestionResource> {

    @Input(placeholder = "Page", type = "text")
    private UUID page;

    @Input(placeholder = "Topic", type = "text")
    private String topic;

    @Input(placeholder = "Text", type = "editor")
    @Dom
    private Editor editor = new Editor();

    @Input(placeholder = "Owner", type = "lazyselect")
    private UserResource owner;

    @Input(placeholder = "Views", type = "number")
    private int views;

    @Input(placeholder = "Created", type = "datetime-local")
    private LocalDateTime created;

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

    public UserResource getOwner() {
        return owner;
    }

    public void setOwner(UserResource owner) {
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

    public static QuestionResource factory(Question question) {
        QuestionResource resource = new QuestionResource();

        resource.setId(question.getId());
        resource.setCreated(LocalDateTime.ofInstant(question.getCreated(), ZoneId.systemDefault()));
        resource.setTopic(question.getTopic());
        resource.setEditor(Editor.factory(question.getHtml(), question.getText()));
        resource.setPage(question.getPage().getId());
        resource.setOwner(UserResource.factory(question.getOwner()));
        resource.setViews(question.getViews());

        return resource;
    }

    public static Question updater(QuestionResource resource, Question question, Identity identity, EntityManager entityManager) {
        question.setTopic(resource.getTopic());
        question.setPage(entityManager.find(Page.class, resource.getPage()));
        question.setHtml(resource.getEditor().getHtml());
        question.setText(resource.getEditor().getText());
        question.setViews(resource.getViews());
        question.setOwner(identity.getUser());
        return question;
    }
}
