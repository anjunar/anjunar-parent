package de.bitvale.anjunar.timeline;


import javax.persistence.Entity;

@Entity
public class TextPost extends AbstractPost {

    @Override
    public <E> E accept(AbstractPostVisitor<E> visitor) {
        return visitor.visit(this);
    }
}
