package de.bitvale.common.rest.api.meta.validator;

public class ConstraintValidator {

    private final String name;

    private final String message;

    public ConstraintValidator(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

}
