package de.bitvale.common.rest.api.meta;

import java.util.List;
import java.util.Locale;

public class MetaProperty extends Property {

    private final MetaForm form;

    public MetaProperty(Class<?> instance, String name, String type, boolean naming, boolean primaryKey) {
        super(name, type, naming, primaryKey);

        form = new MetaForm(instance);
    }

    public List<Property> getProperties() {
        return form.getProperties();
    }

}
