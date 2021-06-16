package de.industrialsociety.anjunar.timeline;

import de.industrialsociety.common.security.User;
import de.industrialsociety.common.ddd.AbstractAggregate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ti_comment")
public class Comment extends AbstractAggregate {

    @Lob
    private String text;

    @ManyToOne
    private Post post;

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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
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
