package de.bitvale.common.rest.api.search.predicate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.search.PredicateVisitor;

import javax.persistence.criteria.Expression;

@JsonTypeName("subQuery")
public class SubQueryExpression extends AbstractExpression {

    private final RestExpression expression;
    private final String from;
    private final String path;

    @JsonCreator
    public SubQueryExpression(@JsonProperty("from") String from,
                              @JsonProperty("path") String path,
                              @JsonProperty("expression") RestExpression expression,
                              @JsonProperty("links") Link... links) {
        super(links);
        this.expression = expression;
        this.from = from;
        this.path = path;
    }

    public RestExpression getExpression() {
        return expression;
    }

    public String getFrom() {
        return from;
    }

    public String getPath() {
        return path;
    }

    @Override
    public Expression<?> accept(PredicateVisitor visitor) {
        return visitor.visitSubQuery(this);
    }
}
