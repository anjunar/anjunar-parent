package de.bitvale.anjunar.timeline;

import de.bitvale.common.security.User;
import de.bitvale.common.ddd.AbstractAggregate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "do_comment")
public class Comment extends AbstractAggregate {

    @Lob
    private String text;

    @ManyToOne
    private AbstractPost post;

    @ManyToOne
    private User owner;

    @ManyToMany
    private final Set<User> likes = new HashSet<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AbstractPost getPost() {
        return post;
    }

    public void setPost(AbstractPost post) {
        this.post = post;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<User> getLikes() {
        return likes;
    }

}
