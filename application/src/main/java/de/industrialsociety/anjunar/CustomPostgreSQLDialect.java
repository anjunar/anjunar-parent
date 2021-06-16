package de.industrialsociety.anjunar;

import de.industrialsociety.anjunar.sqlfunction.PostgreSQLFTSFunction;
import de.industrialsociety.anjunar.sqlfunction.PostgreSQLLevenshteinFunction;
import org.hibernate.dialect.PostgreSQL9Dialect;

public class CustomPostgreSQLDialect extends PostgreSQL9Dialect {

    public CustomPostgreSQLDialect () {
        registerFunction("levenshtein", new PostgreSQLLevenshteinFunction());
        registerFunction("fts", new PostgreSQLFTSFunction());
    }

}