package de.bitvale.common.rest.api.search.predicate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.search.PredicateVisitor;

import javax.persistence.criteria.Expression;
import java.util.List;

@JsonTypeName("concat")
public class ConcatExpression extends AbstractExpression {

    private final RestExpression expression;

    private final List<String> paths;

    @JsonCreator
    public ConcatExpression(@JsonProperty("expression") RestExpression expression,
                            @JsonProperty("paths") List<String> paths,
                            @JsonProperty("links") Link... links) {
        super(links);
        this.expression = expression;
        this.paths = paths;
    }

    public RestExpression getExpression() {
        return expression;
    }

    public List<String> getPaths() {
        return paths;
    }

    @Override
    public Expression<?> accept(PredicateVisitor visitor) {
        return visitor.visitConcat(this);
    }
}
