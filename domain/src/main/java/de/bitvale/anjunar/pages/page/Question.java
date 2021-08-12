package de.bitvale.anjunar.pages.page;

import de.bitvale.common.security.User;
import de.bitvale.common.ddd.AbstractAggregate;
import de.bitvale.anjunar.pages.Page;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "do_question")
public class Question extends AbstractAggregate {

    @Lob
    private String topic;

    @Lob
    private String html;

    @Lob
    private String text;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Page page;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private final List<Answer> replies = new ArrayList<>();

    @Column(name = "do_views")
    private int views = 0;

    @ManyToMany
    private final Set<User> likes = new HashSet<>();

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

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

    public List<Answer> getReplies() {
        return replies;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Set<User> getLikes() {
        return likes;
    }
}
