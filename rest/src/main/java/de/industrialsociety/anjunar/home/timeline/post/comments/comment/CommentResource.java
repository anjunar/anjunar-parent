package de.industrialsociety.anjunar.home.timeline.post.comments.comment;

import de.industrialsociety.common.rest.api.AbstractRestEntity;
import de.industrialsociety.anjunar.shared.users.user.UserResource;
import de.industrialsociety.common.rest.api.meta.Input;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CommentResource extends AbstractRestEntity {

    @Input(placeholder = "Text", type = "text")
    private String text;

    private UUID post;

    @Input(placeholder = "Owner", type = "lazyselect")
    private UserResource owner;

    @Input(placeholder = "Likes", type = "lazymultiselect")
    private final Set<UserResource> likes = new HashSet<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getPost() {
        return post;
    }

    public void setPost(UUID post) {
        this.post = post;
    }

    public UserResource getOwner() {
        return owner;
    }

    public void setOwner(UserResource owner) {
        this.owner = owner;
    }

    public Set<UserResource> getLikes() {
        return likes;
    }
}
