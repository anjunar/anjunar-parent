package de.bitvale.anjunar.pages.page;

import de.bitvale.common.security.User;
import de.bitvale.common.ddd.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "do_answer")
public class Answer extends AbstractEntity {

    @Lob
    private String text;

    @Lob
    private String html;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Question question;

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

    public Question getTopic() {
        return question;
    }

    public void setTopic(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
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
