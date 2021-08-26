package de.bitvale.anjunar.control.users.search;

import javax.persistence.criteria.Predicate;

public interface PredicateVisitor {
    Predicate visit(AndPredicate predicate);

    Predicate visit(BirthdatePredicate predicate);

    Predicate visit(EMailPredicate predicate);

    Predicate visit(FirstNamePredicate firstName);

    Predicate visit(LastNamePredicate predicate);

    Predicate visit(NamingPredicate predicate);

    Predicate visit(OrPredicate predicate);
}
