package de.bitvale.anjunar.home.timeline.post;

import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.meta.Input;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PostResource extends AbstractRestEntity<PostResource> {

    @Input(placeholder = "Text", type = "text")
    private String text;

    @Input(placeholder = "Image", type = "image")
    private Blob image;

    @Input(placeholder = "Owner", type = "lazyselect")
    private UserResource owner;

    @Input(placeholder = "Created", type = "datetime-local")
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
