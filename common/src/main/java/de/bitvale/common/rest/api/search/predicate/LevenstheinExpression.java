package de.bitvale.common.rest.api.search.predicate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.search.PredicateVisitor;

import javax.persistence.criteria.Expression;

@JsonTypeName("levensthein")
public class LevenstheinExpression extends AbstractExpression {

    private final String value;

    private final RestExpression expression;

    @JsonCreator
    public LevenstheinExpression(@JsonProperty("value") String value,
                                 @JsonProperty("expression") RestExpression expression,
                                 @JsonProperty("links") Link... links) {
        super(links);
        this.value = value;
        this.expression = expression;
    }

    public String getValue() {
        return value;
    }

    public RestExpression getExpression() {
        return expression;
    }

    @Override
    public Expression<?> accept(PredicateVisitor visitor) {
        return visitor.visitLevensthein(this);
    }
}
