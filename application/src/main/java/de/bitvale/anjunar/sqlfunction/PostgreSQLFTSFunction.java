package de.bitvale.anjunar.sqlfunction;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

import java.util.List;

public class PostgreSQLFTSFunction implements SQLFunction {

    @Override

    public boolean hasArguments() {
        return true;

    }

    @Override

    public boolean hasParenthesesIfNoArguments() {
        return false;
    }

    @Override

    public Type getReturnType(final Type firstArgumentType, final Mapping mapping) throws QueryException {
        return new BooleanType();
    }

    @Override

    public String render(Type firstArgumentType, List args, SessionFactoryImplementor factory) throws QueryException {

        if (args== null || args.size() < 2) {
            throw new IllegalArgumentException("The function must be passed at least 2 arguments");
        }

        String fragment = null;
        String ftsConfiguration = null;
        String field = null;
        String value = null;

        if (args.size() == 3) {

            ftsConfiguration = (String) args.get(0);
            field = (String) args.get(1);
            value = (String) args.get(2);
            fragment = "to_tsvector(" + ftsConfiguration + ", " + field + ") @@ " + "plainto_tsquery(" + ftsConfiguration + ", " + value + ")";

        } else {
            field = (String) args.get(0);
            value = (String) args.get(1);
            fragment = "to_tsvector(" + field + ") @@ " + "plainto_tsquery('" + value + "')";

        }

        return fragment;

    }

}