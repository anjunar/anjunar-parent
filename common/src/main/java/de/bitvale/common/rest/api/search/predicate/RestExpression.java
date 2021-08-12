package de.bitvale.common.rest.api.search.predicate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.bitvale.common.rest.api.search.PredicateVisitor;

import javax.persistence.criteria.Expression;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(AndExpression.class),
        @JsonSubTypes.Type(ConcatExpression.class),
        @JsonSubTypes.Type(EqualExpression.class),
        @JsonSubTypes.Type(InExpression.class),
        @JsonSubTypes.Type(InSelectExpression.class),
        @JsonSubTypes.Type(IsNullExpression.class),
        @JsonSubTypes.Type(LevenstheinExpression.class),
        @JsonSubTypes.Type(LikeExpression.class),
        @JsonSubTypes.Type(NoopExpression.class),
        @JsonSubTypes.Type(NotExpression.class),
        @JsonSubTypes.Type(OrExpression.class),
        @JsonSubTypes.Type(PathExpression.class),
        @JsonSubTypes.Type(SubQueryExpression.class)
})
public interface RestExpression {

    Expression<?> accept(PredicateVisitor visitor);

}
