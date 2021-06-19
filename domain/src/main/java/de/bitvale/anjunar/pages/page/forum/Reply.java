package de.bitvale.anjunar.pages.page.forum;

import de.bitvale.common.security.User;
import de.bitvale.common.ddd.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Reply extends AbstractEntity {

    @Lob
    private String text;

    @Lob
    private String html;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Topic topic;

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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
