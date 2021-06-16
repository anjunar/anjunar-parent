package de.industrialsociety.common.rest.api.search;

import de.industrialsociety.common.rest.api.search.sort.LevenstheinExpression;
import de.industrialsociety.common.rest.api.search.sort.NormalExpression;

import javax.persistence.criteria.Order;

public interface SortVisitor {
    Order visit(NormalExpression normalExpression);

    Order visit(LevenstheinExpression levenstheinExpression);
}
