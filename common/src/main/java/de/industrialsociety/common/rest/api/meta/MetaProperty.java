package de.industrialsociety.common.rest.api.meta;

import de.industrialsociety.common.rest.api.Link;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MetaProperty<E> extends Property {

    private final MetaForm<E> form;

    public MetaProperty(E instance, String name, String placeholder, String type, Locale locale) {
        super(name, placeholder, type);

        form = new MetaForm<>(instance, locale);
    }

    public List<Property> getProperties() {
        return form.getProperties();
    }

    public Set<Link> getActions() {
        return form.getActions();
    }

    public boolean addAction(Link link) {
        return form.addAction(link);
    }

    public Set<Link> getSources() {
        return form.getSources();
    }

    public boolean addSource(Link link) {
        return form.addSource(link);
    }

    public E getForm() {
        return form.getForm();
    }
}
