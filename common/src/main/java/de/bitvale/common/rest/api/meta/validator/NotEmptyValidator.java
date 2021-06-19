package de.bitvale.common.rest.api.meta.validator;

public class NotEmptyValidator extends ConstraintValidator {

    public NotEmptyValidator(String message) {
        super("notEmpty", message);
    }

}
