package de.industrialsociety.common.rest.api.meta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.industrialsociety.common.rest.api.meta.validator.ConstraintValidator;
import de.industrialsociety.common.rest.api.Link;
import de.industrialsociety.common.rest.api.LinksContainer;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Patrick Bittner on 17/04/2017.
 */
public class Property implements LinksContainer {

    private final String name;

    private final String placeholder;

    private final String type;

    private final Set<Link> links = new HashSet<>();

    private final Set<ConstraintValidator> validators = new HashSet<>();

    @JsonCreator
    public Property(@JsonProperty("name") String name,
                    @JsonProperty("placeholder") String placeholder,
                    @JsonProperty("type") String type) {
        this.name = name;
        this.placeholder = placeholder;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getType() {
        return type;
    }

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    public Set<ConstraintValidator> getValidators() {
        return validators;
    }

    @Override
    public boolean addLink(Link link) {
        return links.add(link);
    }

    public boolean addValidator(ConstraintValidator validator) {
        return validators.add(validator);
    }

}
