package de.bitvale.anjunar.home.timeline.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.bitvale.anjunar.control.user.timeline.UserPost;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.anjunar.timeline.Post;
import de.bitvale.anjunar.timeline.TimelineImage;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PostResource extends AbstractRestEntity<PostResource> {

    @Input(placeholder = "Text", type = "text")
    private String text;

    @Input(placeholder = "Image", type = "image")
    private Blob image = new Blob();

    @Input(placeholder = "Owner", type = "lazyselect")
    private UserResource owner;

    @Input(placeholder = "Created", type = "datetime-local")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
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

    public static PostResource factory(Post post) {
        PostResource resource = new PostResource();

        resource.setId(post.getId());
        resource.setText(post.getText());
        resource.setCreated(post.getCreated());
        resource.setImage(Blob.factory(post.getImage()));
        resource.setOwner(UserResource.factory(post.getOwner()));

        for (User user : post.getLikes()) {
            resource.getLikes().add(UserResource.factory(user));
        }

        return resource;
    }

    public static Post updater(PostResource resource, UserPost post, Identity identity, EntityManager entityManager) {
        post.setOwner(identity.getUser());
        post.setUser(identity.getUser());
        post.setImage((TimelineImage) Blob.updater(resource.getImage(), post.getImage()));
        post.setText(resource.getText());
        post.getLikes().clear();
        for (UserResource like : resource.getLikes()) {
            post.getLikes().add(entityManager.find(User.class, like.getId()));
        }
        return post;
    }
}
