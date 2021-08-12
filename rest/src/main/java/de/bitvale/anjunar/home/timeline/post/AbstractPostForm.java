package de.bitvale.anjunar.home.timeline.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.anjunar.timeline.AbstractPost;
import de.bitvale.anjunar.timeline.Image;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImagePostForm.class, name = "Image"),
        @JsonSubTypes.Type(value = LinkPostForm.class, name = "Link"),
        @JsonSubTypes.Type(value = TextPostForm.class, name = "Text"),
        @JsonSubTypes.Type(value = SystemPostForm.class, name = "System")}
)
public abstract class AbstractPostForm extends AbstractRestEntity {

    @Input(type = "textarea")
    private String text;

    @Input(type = "lazyselect")
    private UserSelect owner;

    @Input(type = "datetime-local")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Instant created;

    @Input(type = "lazymultiselect")
    private final Set<UserSelect> likes = new HashSet<>();

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

    public Set<UserSelect> getLikes() {
        return likes;
    }

    public static AbstractPostForm abstractFactory(AbstractPostForm resource, AbstractPost post) {
        resource.setId(post.getId());
        resource.setText(post.getText());
        resource.setCreated(post.getCreated());
        resource.setOwner(UserSelect.factory(post.getOwner()));

        for (User user : post.getLikes()) {
            resource.getLikes().add(UserSelect.factory(user));
        }

        return resource;
    }

    public static AbstractPost abstractUpdater(AbstractPostForm resource, AbstractPost post, Identity identity, EntityManager entityManager) {
        post.setOwner(identity.getUser());
        post.setText(resource.getText());
        post.getLikes().clear();
        for (UserSelect like : resource.getLikes()) {
            post.getLikes().add(entityManager.find(User.class, like.getId()));
        }
        return post;
    }
}
