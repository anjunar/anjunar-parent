package de.bitvale.anjunar.control.users.search;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;

public class BirthdatePredicate extends AbstractPredicate {

    private LocalDate start;

    private LocalDate end;

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    @Override
    Predicate accept(PredicateVisitor visitor) {
        return visitor.visit(this);
    }
}
