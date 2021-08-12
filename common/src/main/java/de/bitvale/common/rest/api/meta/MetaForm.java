package de.bitvale.common.rest.api.meta;

import de.bitvale.common.security.Identity;
import de.bitvale.introspector.bean.BeanIntrospector;
import de.bitvale.introspector.bean.BeanModel;
import de.bitvale.introspector.bean.BeanProperty;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.validation.Constraint;
import java.lang.annotation.Annotation;
import java.util.*;

public class MetaForm {

    private final List<Property> properties = new ArrayList<>();

    public MetaForm(Class<?> instance) {
        Instance<Identity> instanceIdentity = CDI.current().select(Identity.class);
        Identity identity = instanceIdentity.stream().findAny().orElse(null);
        Locale locale = identity.getLanguage();

        BeanModel<?> model = BeanIntrospector.create(instance);

        for (BeanProperty<?, ?> beanProperty : model.getProperties()) {
            Input type = beanProperty.getAnnotation(Input.class);
            if (type == null) {
                Property property = new Property(beanProperty.getKey(),"view", false, false);
                properties.add(property);
            } else {
                if (! type.ignore()) {
                    if (type.type().equals("form")) {
                        MetaProperty property = new MetaProperty(beanProperty.getType().getRawType(), beanProperty.getKey(), type.type(), type.naming(), type.primaryKey());
                        properties.add(property);
                    } else {
                        Property property = new Property(beanProperty.getKey(), type.type(), type.naming(), type.primaryKey());
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
