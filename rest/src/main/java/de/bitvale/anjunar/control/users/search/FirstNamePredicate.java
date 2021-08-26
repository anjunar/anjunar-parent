package de.bitvale.anjunar.control.users.search;

import javax.persistence.criteria.Predicate;

public class FirstNamePredicate extends AbstractPredicate {

    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    Predicate accept(PredicateVisitor visitor) {
        return visitor.visit(this);
    }
}
