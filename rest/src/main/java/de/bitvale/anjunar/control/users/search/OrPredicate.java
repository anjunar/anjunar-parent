package de.bitvale.anjunar.control.users.search;

import javax.persistence.criteria.Predicate;
import java.util.List;

public class OrPredicate extends AbstractPredicate {

    private List<AbstractPredicate> predicates;

    public List<AbstractPredicate> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<AbstractPredicate> predicates) {
        this.predicates = predicates;
    }

    @Override
    Predicate accept(PredicateVisitor visitor) {
        return visitor.visit(this);
    }
}
