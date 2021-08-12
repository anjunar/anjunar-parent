package de.bitvale.common.rest.api.meta.validator;

public class MaxValidator extends ConstraintValidator {

    private final Long value;

    public MaxValidator(String message, Long value) {
        super("max", message);
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

}
