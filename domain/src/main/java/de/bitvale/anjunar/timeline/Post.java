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
@Inheritance(strategy = InheritanceType.JOINED)
public class Post extends AbstractAggregate {

    @Lob
    private String text;

    @ManyToOne(cascade = CascadeType.ALL)
    private TimelineImage image;

    @ManyToOne
    private User owner;

    @ManyToMany(cascade = CascadeType.ALL)
    private final Set<User> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TimelineImage getImage() {
        return image;
    }

    public void setImage(TimelineImage image) {
        this.image = image;
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
