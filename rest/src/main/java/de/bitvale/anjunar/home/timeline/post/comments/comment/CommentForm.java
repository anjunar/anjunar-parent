package de.bitvale.anjunar.home.timeline.post.comments.comment;

import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.anjunar.timeline.AbstractPost;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CommentForm extends AbstractRestEntity {

    @Input(type = "textarea")
    private String text;

    @Input(type = "text")
    private UUID post;

    @Input(type = "lazyselect")
    private UserSelect owner;

    @Input(type = "lazymultiselect")
    private final Set<UserSelect> likes = new HashSet<>();

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

    public UserSelect getOwner() {
        return owner;
    }

    public void setOwner(UserSelect owner) {
        this.owner = owner;
    }

    public Set<UserSelect> getLikes() {
        return likes;
    }

    public static CommentForm factory(Comment comment) {
        CommentForm resource = new CommentForm();

        resource.setId(comment.getId());
        resource.setText(comment.getText());
        resource.setPost(comment.getPost().getId());

        resource.setOwner(UserSelect.factory(comment.getOwner()));

        for (User like : comment.getLikes()) {
            resource.getLikes().add(UserSelect.factory(like));
        }

        return resource;
    }

    public static Comment updater(CommentForm resource, Comment comment, Identity identity, EntityManager entityManager) {
        comment.setOwner(identity.getUser());
        comment.setPost(entityManager.find(AbstractPost.class, resource.getPost()));
        comment.setText(resource.getText());
        comment.getLikes().clear();
        for (UserSelect like : resource.getLikes()) {
            comment.getLikes().add(entityManager.find(User.class, like.getId()));
        }
        return comment;
    }
}
