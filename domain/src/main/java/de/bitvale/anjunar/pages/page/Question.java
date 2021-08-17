package de.bitvale.anjunar.pages.page;

import de.bitvale.anjunar.shared.Likeable;
import de.bitvale.common.ddd.AbstractEntity;
import de.bitvale.common.security.User;
import de.bitvale.anjunar.pages.Page;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "do_question")
public class Question extends Likeable {

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

}
