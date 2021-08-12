package de.bitvale.anjunar.pages;

import de.bitvale.common.ddd.AbstractAggregate;
import de.bitvale.common.security.User;
import org.hibernate.envers.*;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Audited
@Table(name = "do_page")
@AuditOverrides(value = {
        @AuditOverride(forClass = Page.class),
        @AuditOverride(forClass = AbstractAggregate.class),
})
public class Page extends AbstractAggregate {

    private String title;

    @Lob
    private String content;

    @Lob
    private String text;

    @ManyToOne
    @Audited(targetAuditMode = NOT_AUDITED)
    private User modifier;

    @Audited(targetAuditMode = NOT_AUDITED)
    private Locale language;

    @OneToMany()
    @Audited(targetAuditMode = NOT_AUDITED)
    private final Set<Page> links = new HashSet<>();

    @ManyToMany
    @Audited(targetAuditMode = NOT_AUDITED)
    private final Set<User> likes = new HashSet<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getModifier() {
        return modifier;
    }

    public void setModifier(User modifier) {
        this.modifier = modifier;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public Set<Page> getLinks() {
        return links;
    }

    public Set<User> getLikes() {
        return likes;
    }
}
