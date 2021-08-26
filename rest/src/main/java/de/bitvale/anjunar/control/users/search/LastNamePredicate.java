package de.bitvale.anjunar.control.users.search;

import javax.persistence.criteria.Predicate;

public class LastNamePredicate extends AbstractPredicate {

    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    Predicate accept(PredicateVisitor visitor) {
        return visitor.visit(this);
    }
}
