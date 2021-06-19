package de.bitvale.anjunar.pages.page.forum.topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;

import java.time.Instant;
import java.util.UUID;

public class PageTopicResource extends AbstractRestEntity {

    @Input(placeholder = "Page", type = "text")
    private UUID page;

    @Input(placeholder = "Topic", type = "text")
    private String topic;

    @Input(placeholder = "Text", type = "editor")
    private Editor editor;

    @Input(placeholder = "Owner", type = "lazyselect")
    private UserResource owner;

    @Input(placeholder = "Views", type = "number")
    private int views;

    @Input(placeholder = "Created", type = "datetime-local")
    @JsonFormat(pattern = "YYYY-MM-dd'T'HH:mm",  timezone = "UTC")
    private Instant created;

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

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
