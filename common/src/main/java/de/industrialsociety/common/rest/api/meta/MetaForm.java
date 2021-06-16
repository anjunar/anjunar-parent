package de.industrialsociety.common.rest.api.meta;

import de.industrialsociety.common.rest.api.ActionsContainer;
import de.industrialsociety.common.rest.api.Link;
import de.industrialsociety.common.rest.api.LinksContainer;
import de.industrialsociety.common.rest.api.SourcesContainer;
import de.industrialsociety.introspector.bean.BeanIntrospector;
import de.industrialsociety.introspector.bean.BeanModel;
import de.industrialsociety.introspector.bean.BeanProperty;

import javax.validation.Constraint;
import java.lang.annotation.Annotation;
import java.util.*;

public class MetaForm<E> implements LinksContainer, ActionsContainer, SourcesContainer {

    private final List<Property> properties = new ArrayList<>();

    private final Set<Link> links = new HashSet<>();

    private final Set<Link> actions = new HashSet<>();

    private final Set<Link> sources = new HashSet<>();

    private final E form;

    public MetaForm(E instance, Locale locale) {
        form = instance;
        BeanModel<E> model = BeanIntrospector.create((Class<E>) instance.getClass());

        for (BeanProperty<E, ?> beanProperty : model.getProperties()) {
            Input type = beanProperty.getAnnotation(Input.class);
            if (type == null) {
                Property property = new Property(beanProperty.getKey(), beanProperty.getKey(), "view");
                properties.add(property);
            } else {
                if (! type.ignore()) {
                    ResourceBundle placeholderBundle = ResourceBundle.getBundle("de.industrialsociety.anjunar.PlaceholderMessages", locale);
                    String placeholder = "";
                    if (type.placeholder().startsWith("de.industrialsociety")) {
                        placeholder = placeholderBundle.getString(type.placeholder());
                    } else {
                        placeholder = type.placeholder();
                    }
                    if (type.type().equals("form")) {
                        MetaProperty<?> property = new MetaProperty<>(beanProperty.apply(instance), beanProperty.getKey(), placeholder, type.type(), locale);
                        properties.add(property);
                    } else {
                        Property property = new Property(beanProperty.getKey(), placeholder, type.type());
                        for (Annotation annotation : beanProperty.getAnnotations()) {
                            if (annotation.annotationType().isAnnotationPresent(Constraint.class)) {
                                ResourceBundle validationBundle = ResourceBundle.getBundle("org.hibernate.validator.ValidationMessages", locale);

                                ConstraintHelper.processConstraints(beanProperty, property, validationBundle);
                            }
                        }
                        properties.add(property);
                    }
                }

            }
        }
    }

    public List<Property> getProperties() {
        return properties;
    }

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    public Set<Link> getActions() {
        return actions;
    }

    @Override
    public boolean addLink(Link link) {
        return links.add(link);
    }

    public boolean addAction(Link link) {
        return actions.add(link);
    }

    @Override
    public Set<Link> getSources() {
        return sources;
    }

    @Override
    public boolean addSource(Link link) {
        return sources.add(link);
    }

    public E getForm() {
        return form;
    }

    public Property find(String property) {
        return properties
                .stream()
                .filter((prop) -> prop.getName().equals(property))
                .findAny()
                .orElse(null);
    }
}
