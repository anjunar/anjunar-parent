package de.bitvale.anjunar.pages.page.questions.question.answers.answer;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.bitvale.anjunar.pages.page.Answer;
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
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AnswerForm extends AbstractLikeableRestEntity {

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

    public Set<UserSelect> getLikes() {
        return likes;
    }

    private static class AnswerFormConverter extends AbstractLikeableRestEntityConverter<Answer, AnswerForm> {

        public static AnswerFormConverter INSTANCE = new AnswerFormConverter();

        public AnswerForm factory(AnswerForm resource, Answer answer) {
            resource.setId(answer.getId());
            resource.setEditor(Editor.factory(answer.getHtml(), answer.getText()));
            resource.setTopic(answer.getTopic().getId());
            resource.setCreated(answer.getCreated());
            resource.setOwner(UserSelect.factory(answer.getOwner()));
            return resource;
        }

        @Override
        public Answer updater(AnswerForm form, Answer answer, EntityManager entityManager, Identity identity) {
            Question question = entityManager.find(Question.class, form.getTopic());
            answer.setText(form.getEditor().getText());
            answer.setHtml(form.getEditor().getHtml());
            answer.setTopic(question);
            User owner = entityManager.find(User.class, form.getOwner().getId());
            answer.setOwner(owner);
            return super.updater(form, answer, entityManager, identity);
        }

    }

    public static AnswerForm factory(Answer answer) {
        return AnswerFormConverter.INSTANCE.factory(new AnswerForm(), answer);
    }

    public static void update(Answer answer, AnswerForm form, Identity identity, EntityManager entityManager) {
        AnswerFormConverter.INSTANCE.updater(form, answer, entityManager, identity);
    }

}
