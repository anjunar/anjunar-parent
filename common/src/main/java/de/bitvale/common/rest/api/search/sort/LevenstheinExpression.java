package de.bitvale.common.rest.api.search.sort;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.bitvale.common.rest.api.search.SortVisitor;

import javax.persistence.criteria.Order;

@JsonTypeName("levensthein")
public class LevenstheinExpression implements SortExpression {

    private String[] paths;

    private Boolean asc;

    private String value;

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }

    public Boolean getAsc() {
        return asc;
    }

    public void setAsc(Boolean asc) {
        this.asc = asc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Order accept(SortVisitor visitor) {
        return visitor.visit(this);
    }
}
