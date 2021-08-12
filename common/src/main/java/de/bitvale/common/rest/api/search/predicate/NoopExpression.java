package de.bitvale.common.rest.api.search.predicate;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.bitvale.common.rest.api.search.PredicateVisitor;

import javax.persistence.criteria.Expression;

@JsonTypeName("noop")
public class NoopExpression extends AbstractExpression {
    @Override
    public Expression<?> accept(PredicateVisitor visitor) {
        return visitor.visitNoop(this);
    }
}
