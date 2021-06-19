package de.bitvale.common.rest.api.meta.validator;

public class MinValidator extends ConstraintValidator {

    private final Long value;

    public MinValidator(String message, Long value) {
        super("min", message);
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

}
