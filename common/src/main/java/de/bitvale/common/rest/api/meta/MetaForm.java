package de.bitvale.common.rest.api.meta;

import de.bitvale.introspector.bean.BeanIntrospector;
import de.bitvale.introspector.bean.BeanModel;
import de.bitvale.introspector.bean.BeanProperty;
import jakarta.validation.Constraint;

import java.lang.annotation.Annotation;
import java.util.*;

public class MetaForm<E> {

    private final List<Property> properties = new ArrayList<>();

    public MetaForm(Class<E> instance, Locale locale) {
        BeanModel<E> model = BeanIntrospector.create(instance);

        for (BeanProperty<E, ?> beanProperty : model.getProperties()) {
            Input type = beanProperty.getAnnotation(Input.class);
            if (type == null) {
                Property property = new Property(beanProperty.getKey(), beanProperty.getKey(), "view");
                properties.add(property);
            } else {
                if (! type.ignore()) {
                    ResourceBundle placeholderBundle = ResourceBundle.getBundle("de.bitvale.anjunar.PlaceholderMessages", locale);
                    String placeholder = "";
                    if (type.placeholder().startsWith("de.bitvale")) {
                        placeholder = placeholderBundle.getString(type.placeholder());
                    } else {
                        placeholder = type.placeholder();
                    }
                    if (type.type().equals("form")) {
                        MetaProperty<?> property = new MetaProperty<>(beanProperty.getType().getRawType(), beanProperty.getKey(), placeholder, type.type(), locale);
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

    public Property find(String property) {
        return properties
                .stream()
                .filter((prop) -> prop.getName().equals(property))
                .findAny()
                .orElse(null);
    }
}
