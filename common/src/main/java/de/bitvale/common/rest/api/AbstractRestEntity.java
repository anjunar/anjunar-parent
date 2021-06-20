package de.bitvale.common.rest.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.reflect.TypeToken;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.rest.api.meta.MetaForm;
import de.bitvale.common.security.Identity;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractRestEntity<E> implements RestEntity, ActionsContainer, LinksContainer, SourcesContainer {

    @Input(placeholder = "Id", type = "text")
    private UUID id;

    @Input(ignore = true)
    private Set<Link> actions = new HashSet<>();

    @Input(ignore = true)
    private Set<Link> links = new HashSet<>();

    @Input(ignore = true)
    private Set<Link> sources = new HashSet<>();

    @Input(ignore = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MetaForm<E> meta;

    public AbstractRestEntity() {
        Instance<Identity> instance = CDI.current().select(Identity.class);
        Identity identity = instance.stream().findAny().orElse(null);
        meta = new MetaForm<>(getEntityClass(), identity.getLanguage());
    }

    @JsonIgnore
    @Input(ignore = true)
    public Class<E> getEntityClass() {
        return (Class<E>) TypeToken.of(getClass()).resolveType(AbstractRestEntity.class.getTypeParameters()[0]).getRawType();
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

    public MetaForm<E> getMeta() {
        return meta;
    }

}
