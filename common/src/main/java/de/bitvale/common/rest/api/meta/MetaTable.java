package de.bitvale.common.rest.api.meta;

import de.bitvale.common.rest.api.ActionsContainer;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.LinksContainer;
import de.bitvale.common.rest.api.SourcesContainer;
import de.bitvale.introspector.bean.BeanIntrospector;
import de.bitvale.introspector.bean.BeanModel;
import de.bitvale.introspector.bean.BeanProperty;
import jakarta.validation.Constraint;

import java.lang.annotation.Annotation;
import java.util.*;

public class MetaTable<E> implements LinksContainer, ActionsContainer, SourcesContainer {

    private final List<Property> columns = new ArrayList<>();

    private final Set<Link> links = new HashSet<>();

    private final Set<Link> actions = new HashSet<>();

    private final Set<Link> sources = new HashSet<>();

    private Sortable[] sortable = new Sortable[]{};

    public MetaTable(Class<E> clazz, Locale locale) {
        BeanModel<E> model = BeanIntrospector.create(clazz);

        for (BeanProperty<E, ?> beanProperty : model.getProperties()) {
            Input type = beanProperty.getAnnotation(Input.class);
            if (type != null) {
                if (! type.ignore()) {
                    ResourceBundle placeholderBundle = ResourceBundle.getBundle("de.bitvale.anjunar.PlaceholderMessages", locale);
                    String placeholder = "";
                    if (type.placeholder().startsWith("de.bitvale")) {
                        placeholder = placeholderBundle.getString(type.placeholder());
                    } else {
                        placeholder = type.placeholder();
                    }
                    Property property = new Property(beanProperty.getKey(), placeholder, type.type());
                    for (Annotation annotation : beanProperty.getAnnotations()) {
                        if (annotation.annotationType().isAnnotationPresent(Constraint.class)) {
                            ResourceBundle bundle = ResourceBundle.getBundle("org.hibernate.validator.ValidationMessages", locale);

                            ConstraintHelper.processConstraints(beanProperty, property, bundle);
                        }
                    }
                    columns.add(property);
                }
            } else {
                Property property = new Property(beanProperty.getKey(), beanProperty.getKey(), "view");
                columns.add(property);
            }

        }
    }

    public List<Property> getColumns() {
        if (sortable.length > 0) {
            List<Property> result = new ArrayList<>();
            for (Sortable sort : sortable) {
                for (Property column : columns) {
                    if (column.getName().equals(sort.getProperty())) {
                        result.add(column);
                        break;
                    }
                }
            }
            return result;
        }
        return columns;
    }

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    public Set<Link> getActions() {
        return actions;
    }

    @Override
    public Set<Link> getSources() {
        return sources;
    }

    public Sortable[] getSortable() {
        return sortable;
    }

    public Property find(String name) {
        return columns
                .stream()
                .filter((column) -> column.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    @Override
    public boolean addLink(Link link) {
        return links.add(link);
    }

    public boolean addAction(Link link) {
        return actions.add(link);
    }

    @Override
    public boolean addSource(Link link) {
        return sources.add(link);
    }

    public void addSortable(Sortable[] value) {
        sortable = value;
    }
}
