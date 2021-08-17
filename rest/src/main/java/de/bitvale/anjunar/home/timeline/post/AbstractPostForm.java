package de.bitvale.anjunar.home.timeline.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.bitvale.anjunar.shared.likeable.AbstractLikeableRestEntity;
import de.bitvale.anjunar.shared.likeable.AbstractLikeableRestEntityConverter;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.anjunar.timeline.AbstractPost;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImagePostForm.class, name = "Image"),
        @JsonSubTypes.Type(value = LinkPostForm.class, name = "Link"),
        @JsonSubTypes.Type(value = TextPostForm.class, name = "Text"),
        @JsonSubTypes.Type(value = SystemPostForm.class, name = "System")}
)
public abstract class AbstractPostForm extends AbstractLikeableRestEntity {

    @Input(type = "textarea")
    private String text;

    @Input(type = "lazyselect")
    private UserSelect owner;

    @Input(type = "datetime-local")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Instant created;

    public abstract <E> E accept(AbstractPostFormVisitor<E> visitor);

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserSelect getOwner() {
        return owner;
    }

    public void setOwner(UserSelect owner) {
        this.owner = owner;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public static class AbstractPostFormConverter<E extends AbstractPost, R extends AbstractPostForm> extends AbstractLikeableRestEntityConverter<E, R> {
        public R factory(R resource, E post) {
            resource.setId(post.getId());
            resource.setText(post.getText());
            resource.setOwner(UserSelect.factory(post.getOwner()));
            return super.factory(resource, post);
        }

        public E updater(R resource, E post, EntityManager entityManager, Identity identity) {
            post.setOwner(identity.getUser());
            post.setText(resource.getText());
            return super.updater(resource, post, entityManager, identity);

        }
    }

}
