package de.industrialsociety.anjunar.pages.page.history;

import de.industrialsociety.anjunar.shared.users.user.UserResource;
import de.industrialsociety.common.rest.api.meta.Input;
import de.industrialsociety.common.rest.api.AbstractRestEntity;

import java.time.Instant;

public class PageHistoryResource extends AbstractRestEntity {

    @Input(placeholder = "Title", type = "text")
    private String title;

    @Input(ignore = true)
    private String content;

    @Input(ignore = true)
    private String text;

    @Input(placeholder = "Revision", type = "number")
    private Number revision;

    @Input(placeholder = "Modifier", type = "lazyselect")
    private UserResource modifier;

    @Input(placeholder = "Modified", type = "date")
    private Instant modified;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Number getRevision() {
        return revision;
    }

    public void setRevision(Number revision) {
        this.revision = revision;
    }

    public UserResource getModifier() {
        return modifier;
    }

    public void setModifier(UserResource modifier) {
        this.modifier = modifier;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }
}
