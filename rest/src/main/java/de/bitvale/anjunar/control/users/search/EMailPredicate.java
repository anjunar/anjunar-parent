package de.bitvale.anjunar.control.users.search;

import javax.persistence.criteria.Predicate;

public class EMailPredicate extends AbstractPredicate {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    Predicate accept(PredicateVisitor visitor) {
        return visitor.visit(this);
    }
}
