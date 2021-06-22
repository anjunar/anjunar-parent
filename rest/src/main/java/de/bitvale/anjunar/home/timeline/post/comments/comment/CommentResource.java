package de.bitvale.anjunar.home.timeline.post.comments.comment;

import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.anjunar.timeline.Post;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CommentResource extends AbstractRestEntity<CommentResource> {

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

    public static CommentResource factory(Comment comment) {
        CommentResource resource = new CommentResource();

        resource.setId(comment.getId());
        resource.setText(comment.getText());
        resource.setPost(comment.getPost().getId());

        resource.setOwner(UserResource.factory(comment.getOwner()));

        for (User like : comment.getLikes()) {
            resource.getLikes().add(UserResource.factory(like));
        }

        return resource;
    }

    public static Comment updater(CommentResource resource, Comment comment, Identity identity, EntityManager entityManager) {
        comment.setOwner(identity.getUser());
        comment.setPost(entityManager.find(Post.class, resource.getPost()));
        comment.setText(resource.getText());
        comment.getLikes().clear();
        for (UserResource like : resource.getLikes()) {
            comment.getLikes().add(entityManager.find(User.class, like.getId()));
        }
        return comment;
    }
}
