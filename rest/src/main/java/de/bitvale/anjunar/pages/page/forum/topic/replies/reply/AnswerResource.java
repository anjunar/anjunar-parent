package de.bitvale.anjunar.pages.page.forum.topic.replies.reply;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.bitvale.anjunar.pages.page.forum.Answer;
import de.bitvale.anjunar.pages.page.forum.Question;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.validators.Dom;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class AnswerResource extends AbstractRestEntity<AnswerResource> {

    @Input(placeholder = "Text", type = "editor")
    @Dom
    private Editor editor;

    @Input(placeholder = "Topic", type = "text")
    private UUID topic;

    @Input(placeholder = "Owner", type = "lazyselect")
    private UserResource owner;

    @Input(placeholder = "Created", type = "datetime-local")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Instant created;

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public UUID getTopic() {
        return topic;
    }

    public void setTopic(UUID topic) {
        this.topic = topic;
    }

    public UserResource getOwner() {
        return owner;
    }

    public void setOwner(UserResource owner) {
        this.owner = owner;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public static AnswerResource factory(Answer answer) {
        AnswerResource resource = new AnswerResource();
        resource.setId(answer.getId());
        resource.setEditor(Editor.factory(answer.getHtml(), answer.getText()));
        resource.setTopic(answer.getTopic().getId());
        resource.setCreated(answer.getCreated());
        resource.setOwner(UserResource.factory(answer.getOwner()));
        return resource;
    }

    public static void update(Answer answer, AnswerResource form, Identity identity, EntityManager entityManager) {
        Question question = entityManager.find(Question.class, form.getTopic());
        answer.setText(form.getEditor().getText());
        answer.setHtml(form.getEditor().getHtml());
        answer.setOwner(identity.getUser());
        answer.setTopic(question);
    }

}
