package de.bitvale.common.rest.api.search.sort;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.bitvale.common.rest.api.search.SortVisitor;

import javax.persistence.criteria.Order;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(NormalExpression.class),
        @JsonSubTypes.Type(LevenstheinExpression.class),
})
public interface SortExpression {

    Order accept(SortVisitor visitor);

}
