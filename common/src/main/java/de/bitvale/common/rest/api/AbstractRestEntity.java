package de.bitvale.common.rest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.rest.api.meta.MetaForm;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractRestEntity implements RestEntity, ActionsContainer, LinksContainer, SourcesContainer {

    @Input(type = "text", primaryKey = true)
    private UUID id;

    @Input(ignore = true)
    private Set<Link> actions = new LinkedHashSet<>();

    @Input(ignore = true)
    private Set<Link> links = new LinkedHashSet<>();

    @Input(ignore = true)
    private Set<Link> sources = new LinkedHashSet<>();

    @Input(ignore = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MetaForm meta;

    public AbstractRestEntity() {
        meta = new MetaForm(getClass());
    }

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

    public Set<Link> getSources() {
        return sources;
    }

    @Override
    public boolean addSource(Link link) {
        return sources.add(link);
    }

    public MetaForm getMeta() {
        return meta;
    }

}
