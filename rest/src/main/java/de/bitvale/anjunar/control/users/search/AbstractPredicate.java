package de.bitvale.anjunar.control.users.search;

import javax.persistence.criteria.Predicate;

public abstract class AbstractPredicate {

    abstract Predicate accept(PredicateVisitor visitor);

}
