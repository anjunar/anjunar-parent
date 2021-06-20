package de.bitvale.anjunar.security.logout;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.bitvale.anjunar.security.login.LoginResource;
import de.bitvale.common.rest.api.ActionsContainer;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.LinksContainer;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.rest.api.meta.MetaForm;
import de.bitvale.common.security.Identity;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import java.util.HashSet;
import java.util.Set;

public class LogoutResource implements LinksContainer, ActionsContainer {

    @Input(ignore = true)
    private final Set<Link> actions = new HashSet<>();

    @Input(ignore = true)
    private final Set<Link> links = new HashSet<>();

    @Input(ignore = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final MetaForm<LoginResource> meta;

    public LogoutResource() {
        Instance<Identity> instance = CDI.current().select(Identity.class);
        Identity identity = instance.stream().findAny().orElse(null);
        meta = new MetaForm<>(LoginResource.class, identity.getLanguage());
    }

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    @Override
    public boolean addLink(Link link) {
        return links.add(link);
    }

    @Override
    public Set<Link> getActions() {
        return actions;
    }

    @Override
    public boolean addAction(Link link) {
        return actions.add(link);
    }

    public MetaForm<LoginResource> getMeta() {
        return meta;
    }
}
