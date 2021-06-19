package de.bitvale.common.rest.api.meta;

import de.bitvale.common.rest.api.meta.validator.*;
import de.bitvale.introspector.bean.BeanProperty;
import de.bitvale.common.rest.api.meta.validator.*;

import javax.validation.constraints.*;
import java.util.ResourceBundle;

public class ConstraintHelper {
    static <E> void processConstraints(BeanProperty<E, ?> beanProperty, Property property, ResourceBundle bundle) {
        Min min = beanProperty.getAnnotation(Min.class);
        if (min != null) {
            String bundleString = bundle.getString(min.message().substring(1, min.message().length() - 1));
            property.addValidator(new MinValidator(bundleString.replace("{value}", String.valueOf(min.value())), min.value()));
        }
        Max max = beanProperty.getAnnotation(Max.class);
        if (max != null) {
            String bundleString = bundle.getString((max.message().substring(1, max.message().length() - 1)));
            property.addValidator(new MaxValidator(bundleString.replace("{value}", String.valueOf(max.value())), max.value()));
        }
        Size size = beanProperty.getAnnotation(Size.class);
        if (size != null) {
            String bundleString = bundle.getString((size.message().substring(1, size.message().length() - 1)));
            String replace = bundleString
                    .replace("{min}", String.valueOf(size.min()))
                    .replace("{max}", String.valueOf(size.max()));
            property.addValidator(new SizeValidator(replace, size.min(), size.max()));
        }
        NotEmpty notEmpty = beanProperty.getAnnotation(NotEmpty.class);
        if (notEmpty != null) {
            property.addValidator(new NotEmptyValidator(bundle.getString(notEmpty.message())));
        }
        Email email = beanProperty.getAnnotation(Email.class);
        if (email != null) {
            String bundleString = bundle.getString((email.message().substring(1, email.message().length() - 1)));
            property.addValidator(new EmailValidator(bundleString));
        }

    }
}
