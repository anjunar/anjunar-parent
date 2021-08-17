package de.bitvale.common.mail;

import de.bitvale.common.ddd.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Locale;

@Entity
@Table(name = "co_template")
public class Template extends AbstractEntity {

    private String name;

    private Locale language;

    @Lob
    private String html;

    @Lob
    private String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

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
}
