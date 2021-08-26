package de.bitvale.anjunar.control.users.search;

import javax.persistence.criteria.Predicate;

public class NamingPredicate extends AbstractPredicate{

    private String naming;

    public String getNaming() {
        return naming;
    }

    public void setNaming(String naming) {
        this.naming = naming;
    }

    @Override
    Predicate accept(PredicateVisitor visitor) {
        return visitor.visit(this);
    }
}
