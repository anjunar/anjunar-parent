package de.bitvale.anjunar.pages.page.questions.question.answers.answer;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.bitvale.anjunar.pages.page.Answer;
import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import de.bitvale.common.validators.Dom;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AnswerForm extends AbstractRestEntity {

    @Input(type = "editor")
    @Dom
    private Editor editor;

    @Input(type = "text")
    private UUID topic;

    @Input(type = "lazyselect")
    private UserSelect owner;

    @Input(type = "datetime-local")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Instant created;

    private Integer views;

    @Input(type = "lazymultiselect")
    private final Set<UserSelect> likes = new HashSet<>();

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

    public UserSelect getOwner() {
        return owner;
    }

    public void setOwner(UserSelect owner) {
        this.owner = owner;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Set<UserSelect> getLikes() {
        return likes;
    }

    public static AnswerForm factory(Answer answer) {
        AnswerForm resource = new AnswerForm();
        resource.setId(answer.getId());
        resource.setEditor(Editor.factory(answer.getHtml(), answer.getText()));
        resource.setTopic(answer.getTopic().getId());
        resource.setCreated(answer.getCreated());
        resource.setOwner(UserSelect.factory(answer.getOwner()));
        resource.setViews(answer.getViews());
        for (User like : answer.getLikes()) {
            resource.getLikes().add(UserSelect.factory(like));
        }
        return resource;
    }

    public static void update(Answer answer, AnswerForm form, Identity identity, EntityManager entityManager) {
        Question question = entityManager.find(Question.class, form.getTopic());
        answer.setText(form.getEditor().getText());
        answer.setHtml(form.getEditor().getHtml());
        answer.setTopic(question);
        User owner = entityManager.find(User.class, form.getOwner().getId());
        answer.setOwner(owner);
        answer.setViews(form.getViews());
        answer.getLikes().clear();
        for (UserSelect like : form.getLikes()) {
            answer.getLikes().add(entityManager.find(User.class, like.getId()));
        }
    }

}
