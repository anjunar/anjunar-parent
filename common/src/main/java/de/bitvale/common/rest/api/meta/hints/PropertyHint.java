package de.bitvale.common.rest.api.meta.hints;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertyHint {

    private final String name;

    private final String message;

    @JsonCreator
    public PropertyHint(@JsonProperty("name") String name,
                        @JsonProperty("text") String message) {
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
