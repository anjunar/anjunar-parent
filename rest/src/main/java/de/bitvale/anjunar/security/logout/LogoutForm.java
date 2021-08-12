package de.bitvale.anjunar.security.logout;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.bitvale.common.rest.api.ActionsContainer;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.LinksContainer;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.rest.api.meta.MetaForm;

import java.util.HashSet;
import java.util.Set;

public class LogoutForm implements LinksContainer, ActionsContainer {

    @Input(ignore = true)
    private final Set<Link> actions = new HashSet<>();

    @Input(ignore = true)
    private final Set<Link> links = new HashSet<>();

    @Input(ignore = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final MetaForm meta = new MetaForm(LogoutForm.class);

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

    public MetaForm getMeta() {
        return meta;
    }
}
