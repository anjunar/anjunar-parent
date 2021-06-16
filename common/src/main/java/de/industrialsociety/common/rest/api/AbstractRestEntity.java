package de.industrialsociety.common.rest.api;

import de.industrialsociety.common.rest.api.meta.Input;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractRestEntity implements RestEntity, ActionsContainer, LinksContainer {

    @Input(placeholder = "Id", type = "text")
    private UUID id;

    @Input(ignore = true)
    private Set<Link> actions = new HashSet<>();

    @Input(ignore = true)
    private Set<Link> links = new HashSet<>();

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public Set<Link> getActions() {
        return actions;
    }

    @Override
    public boolean addAction(Link link) {
        return actions.add(link);
    }

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    @Override
    public boolean addLink(Link link) {
        return links.add(link);
    }
}
