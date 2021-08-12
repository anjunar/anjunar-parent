package de.bitvale.common.rest.api.meta;

public class Sortable {

    private final String property;

    private final boolean isSortable;

    private final boolean visible;

    public Sortable(String property, boolean isSortable, boolean visible) {
        this.property = property;
        this.isSortable = isSortable;
        this.visible = visible;
    }

    public String getProperty() {
        return property;
    }

    public boolean isSortable() {
        return isSortable;
    }

    public boolean isVisible() {
        return visible;
    }
}
