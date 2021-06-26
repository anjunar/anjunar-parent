package de.bitvale.anjunar.pages.page.forum.topic.replies.reply;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.validators.Dom;

import java.time.Instant;
import java.util.UUID;

public class TopicReplyResource extends AbstractRestEntity<TopicReplyResource> {

    @Input(placeholder = "Text", type = "editor")
    @Dom
    private Editor editor;

    @Input(placeholder = "Topic", type = "text")
    private UUID topic;

    @Input(placeholder = "Owner", type = "lazyselect")
    private UserResource owner;

    @Input(placeholder = "Created", type = "datetime-local")
    @JsonFormat(pattern = "YYYY-MM-dd'T'HH:mm", timezone = "UTC")
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
}
