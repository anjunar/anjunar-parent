package de.bitvale.common.rest.api.search;

import de.bitvale.common.rest.api.search.predicate.*;
import de.bitvale.common.rest.api.search.predicate.*;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Subquery;

public interface PredicateVisitor {

    Predicate visitAnd(AndExpression and);

    Predicate visitOr(OrExpression or);

    Predicate visitPath(PathExpression path);

    Predicate visitLike(LikeExpression like);

    Predicate visitNoop(NoopExpression noop);

    Predicate visitEqual(EqualExpression equal);

    Predicate visitIn(InExpression in);

    Subquery<?> visitSubQuery(SubQueryExpression subQuery);

    Predicate visitInSelect(InSelectExpression inSelect);

    Predicate visitIsNull(IsNullExpression isNull);

    Predicate visitNot(NotExpression notExpression);

    Expression<?> visitConcat(ConcatExpression concatExpression);

    Expression<?> visitLevensthein(LevenstheinExpression levenstheinExpression);
}
