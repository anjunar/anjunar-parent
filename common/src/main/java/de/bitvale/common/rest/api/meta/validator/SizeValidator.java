package de.bitvale.common.rest.api.meta.validator;

public class SizeValidator extends ConstraintValidator {

    private final Integer min;
    private final Integer max;

    public SizeValidator(String message, Integer min, Integer max) {
        super("size", message);
        this.min = min;
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }
}
