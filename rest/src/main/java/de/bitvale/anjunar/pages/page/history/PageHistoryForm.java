package de.bitvale.anjunar.pages.page.history;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.rest.api.AbstractRestEntity;

import java.time.Instant;

public class PageHistoryForm extends AbstractRestEntity {

    @Input(type = "text")
    private String title;

    @Input(ignore = true)
    private String content;

    @Input(ignore = true)
    private String text;

    @Input(type = "number")
    private Number revision;

    @Input(type = "lazyselect")
    private UserSelect modifier;

    @Input(type = "date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
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

    public UserSelect getModifier() {
        return modifier;
    }

    public void setModifier(UserSelect modifier) {
        this.modifier = modifier;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public static PageHistoryForm factory(Page page, Number revision) {
        PageHistoryForm resource = new PageHistoryForm();
        resource.setId(page.getId());
        resource.setTitle(page.getTitle());
        resource.setContent(page.getContent());
        resource.setText(page.getText());
        resource.setRevision(revision);
        resource.setModified(page.getModified());

        resource.setModifier(UserSelect.factory(page.getModifier()));
        return resource;
    }
}
