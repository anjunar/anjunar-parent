package de.bitvale.common.rest.api.meta;

public class Validator {

    private final String name;

    private final String message;

    public Validator(String name, String message) {
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
