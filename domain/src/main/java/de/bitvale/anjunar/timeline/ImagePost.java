package de.bitvale.anjunar.timeline;

import javax.persistence.*;

@Entity
public class ImagePost extends AbstractPost {

    @ManyToOne(cascade = CascadeType.ALL)
    private Image image;

    @Override
    public <E> E accept(AbstractPostVisitor<E> visitor) {
        return visitor.visit(this);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
