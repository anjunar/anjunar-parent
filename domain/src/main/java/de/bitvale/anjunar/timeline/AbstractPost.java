package de.bitvale.anjunar.timeline;

import de.bitvale.common.security.User;
import de.bitvale.common.ddd.AbstractAggregate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "do_post")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractPost extends AbstractAggregate {

    @Lob
    private String text;

    @ManyToOne
    private User owner;

    @ManyToMany(cascade = CascadeType.ALL)
    private final Set<User> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    public abstract <E> E accept(AbstractPostVisitor<E> visitor);

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public List<Comment> getComments() {
        return comments;
    }
}
