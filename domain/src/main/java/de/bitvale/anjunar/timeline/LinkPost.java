package de.bitvale.anjunar.timeline;

import javax.persistence.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Entity
public class LinkPost extends AbstractPost {

    private URL link;

    private String title;

    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    public URL getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public <E> E accept(AbstractPostVisitor<E> visitor) {
        return visitor.visit(this);
    }

    public void setLink(URL link) {
        this.link = link;
    }
}
