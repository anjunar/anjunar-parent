package de.bitvale.common.rest.api.search.predicate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.search.PredicateVisitor;

import javax.persistence.criteria.Expression;
import java.util.UUID;

@JsonTypeName("equal")
public class EqualExpression extends AbstractExpression {

    private final UUID value;
    private final String name;

    @JsonCreator
    public EqualExpression(@JsonProperty("value") UUID value,
                           @JsonProperty("name") String name,
                           @JsonProperty("links") Link... links) {
        super(links);
        this.value = value;
        this.name = name;
    }

    public UUID getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public Expression<?> accept(PredicateVisitor visitor) {
        return visitor.visitEqual(this);
    }
}
