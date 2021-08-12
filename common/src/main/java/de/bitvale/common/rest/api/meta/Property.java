package de.bitvale.common.rest.api.meta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.bitvale.common.rest.api.meta.hints.PropertyHint;
import de.bitvale.common.rest.api.meta.validator.ConstraintValidator;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.LinksContainer;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Patrick Bittner on 17/04/2017.
 */
public class Property implements LinksContainer {

    private final String name;

    private final String type;

    private final boolean naming;

    private final boolean primaryKey;

    private final Set<Link> links = new HashSet<>();

    private final Set<ConstraintValidator> validators = new HashSet<>();

    private final Set<PropertyHint> hints = new HashSet<>();

    @JsonCreator
    public Property(@JsonProperty("name") String name,
                    @JsonProperty("type") String type,
                    @JsonProperty("naming") boolean naming,
                    @JsonProperty("primaryKey") boolean primaryKey) {
        this.name = name;
        this.type = type;
        this.naming = naming;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isNaming() {
        return naming;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    @Override
    public boolean addLink(Link link) {
        return links.add(link);
    }

    public Set<ConstraintValidator> getValidators() {
        return validators;
    }

    public boolean addValidator(ConstraintValidator validator) {
        return validators.add(validator);
    }

    public Set<PropertyHint> getHints() {
        return hints;
    }

    public boolean addHint(PropertyHint hint) {
        return hints.add(hint);
    }
}
