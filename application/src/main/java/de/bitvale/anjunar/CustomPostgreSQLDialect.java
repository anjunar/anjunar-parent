package de.bitvale.anjunar;

import de.bitvale.anjunar.sqlfunction.PostgreSQLFTSFunction;
import de.bitvale.anjunar.sqlfunction.PostgreSQLLevenshteinFunction;
import org.hibernate.dialect.PostgreSQL9Dialect;

public class CustomPostgreSQLDialect extends PostgreSQL9Dialect {

    public CustomPostgreSQLDialect () {
        registerFunction("levenshtein", new PostgreSQLLevenshteinFunction());
        registerFunction("fts", new PostgreSQLFTSFunction());
    }

}