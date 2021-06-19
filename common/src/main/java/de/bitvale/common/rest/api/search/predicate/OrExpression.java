package de.bitvale.common.rest.api.search.predicate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.search.PredicateVisitor;

import javax.persistence.criteria.Expression;
import java.util.Set;

@JsonTypeName("or")
public class OrExpression extends AbstractExpression {

    private final Set<RestExpression> expressions;

    @JsonCreator
    public OrExpression(@JsonProperty("expressions") Set<RestExpression> expressions,
                        @JsonProperty("links") Link... links) {
        super(links);
        this.expressions = expressions;
    }

    public Set<RestExpression> getExpressions() {
        return expressions;
    }

    @Override
    public Expression<?> accept(PredicateVisitor visitor) {
        return visitor.visitOr(this);
    }
}
