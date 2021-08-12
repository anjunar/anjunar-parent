package de.bitvale.anjunar.sqlfunction;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;

import java.util.List;

public class OracleSQLLevenstheinFunction implements SQLFunction {
    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return false;
    }

    @Override
    public Type getReturnType(Type firstArgumentType, Mapping mapping) throws QueryException {
        return new IntegerType();
    }

    @Override
    public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor factory) throws QueryException {
        if (arguments== null || arguments.size() < 2) {
            throw new IllegalArgumentException("The function must be passed at least 2 arguments");
        }

        String column = (String) arguments.get(0);
        String argument = (String) arguments.get(1);

        return String.format("UTL_MATCH.EDIT_DISTANCE(%s, %s)", column, argument);
    }
}
