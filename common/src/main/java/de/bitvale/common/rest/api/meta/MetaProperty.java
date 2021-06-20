package de.bitvale.common.rest.api.meta;

import java.util.List;
import java.util.Locale;

public class MetaProperty<E> extends Property {

    private final MetaForm<E> form;

    public MetaProperty(Class<E> instance, String name, String placeholder, String type, Locale locale) {
        super(name, placeholder, type);

        form = new MetaForm<>(instance, locale);
    }

    public List<Property> getProperties() {
        return form.getProperties();
    }

}
