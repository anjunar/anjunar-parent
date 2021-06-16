package de.industrialsociety.anjunar.home.timeline.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.industrialsociety.common.rest.api.AbstractRestEntity;
import de.industrialsociety.common.rest.api.Blob;
import de.industrialsociety.anjunar.shared.users.user.UserResource;
import de.industrialsociety.common.rest.api.meta.Input;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class PostResource extends AbstractRestEntity {

    @Input(placeholder = "Text", type = "text")
    private String text;

    @Input(placeholder = "Image", type = "image")
    private Blob image;

    @Input(placeholder = "Owner", type = "lazyselect")
    private UserResource owner;

    @Input(placeholder = "Created", type = "datetime-local")
    @JsonFormat(pattern = "YYYY-MM-dd'T'HH:mm",  timezone = "UTC")
    private Instant created;

    @Input(placeholder = "Likes", type = "lazymultiselect")
    private final Set<UserResource> likes = new HashSet<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
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

    public Set<UserResource> getLikes() {
        return likes;
    }
}
