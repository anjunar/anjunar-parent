package de.bitvale.common.rest.api.search.predicate;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.bitvale.common.rest.api.search.PredicateVisitor;

import javax.persistence.criteria.Expression;

@JsonTypeName("isNull")
public class IsNullExpression extends AbstractExpression {
    @Override
    public Expression<?> accept(PredicateVisitor visitor) {
        return visitor.visitIsNull(this);
    }
}
