package de.bitvale.anjunar.timeline;

import javax.persistence.Entity;

@Entity
public class SystemPost extends AbstractPost{

    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public <E> E accept(AbstractPostVisitor<E> visitor) {
        return visitor.visit(this);
    }

}
