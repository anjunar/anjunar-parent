package de.bitvale.anjunar.home.timeline.post.comments.comment;

import de.bitvale.anjunar.shared.likeable.AbstractLikeableRestEntity;
import de.bitvale.anjunar.shared.likeable.AbstractLikeableRestEntityConverter;
import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.anjunar.timeline.AbstractPost;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CommentForm extends AbstractLikeableRestEntity {

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

    private static class CommentFormConverter extends AbstractLikeableRestEntityConverter<Comment, CommentForm> {

        public static CommentFormConverter INSTANCE = new CommentFormConverter();

        public CommentForm factory(CommentForm resource, Comment comment) {
            resource.setId(comment.getId());
            resource.setText(comment.getText());
            resource.setPost(comment.getPost().getId());
            resource.setOwner(UserSelect.factory(comment.getOwner()));
            return super.factory(resource, comment);
        }

        public Comment updater(CommentForm resource, Comment comment, EntityManager entityManager, Identity identity) {
            comment.setOwner(identity.getUser());
            comment.setPost(entityManager.find(AbstractPost.class, resource.getPost()));
            comment.setText(resource.getText());
            return super.updater(resource, comment, entityManager, identity);
        }
    }

    public static CommentForm factory(Comment comment) {
        return CommentFormConverter.INSTANCE.factory(new CommentForm(), comment);
    }

    public static Comment updater(CommentForm resource, Comment comment, Identity identity, EntityManager entityManager) {
        return CommentFormConverter.INSTANCE.updater(resource, comment, entityManager, identity);
    }
}
