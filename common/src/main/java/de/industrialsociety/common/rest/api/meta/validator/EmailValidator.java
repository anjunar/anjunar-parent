package de.industrialsociety.common.rest.api.meta.validator;

public class EmailValidator extends ConstraintValidator {

    public EmailValidator(String message) {
        super("email", message);
    }

}
