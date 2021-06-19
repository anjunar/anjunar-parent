package de.bitvale.common.rest.api.search;

import de.bitvale.common.rest.api.search.sort.LevenstheinExpression;
import de.bitvale.common.rest.api.search.sort.NormalExpression;

import javax.persistence.criteria.Order;

public interface SortVisitor {
    Order visit(NormalExpression normalExpression);

    Order visit(LevenstheinExpression levenstheinExpression);
}
