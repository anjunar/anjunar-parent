package de.bitvale.common.rest.api.search.predicate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.search.PredicateVisitor;

import javax.persistence.criteria.Expression;

@JsonTypeName("like")
public class LikeExpression extends AbstractExpression {

    private final String value;
    private final String name;

    @JsonCreator
    public LikeExpression(@JsonProperty("value") String value,
                          @JsonProperty("name") String name,
                          @JsonProperty("links") Link... links) {
        super(links);
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public Expression<?> accept(PredicateVisitor visitor) {
        return visitor.visitLike(this);
    }
}
