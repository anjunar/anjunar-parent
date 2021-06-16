package de.industrialsociety.anjunar.security.logout;

import de.industrialsociety.common.rest.api.ActionsContainer;
import de.industrialsociety.common.rest.api.Link;
import de.industrialsociety.common.rest.api.LinksContainer;
import de.industrialsociety.common.rest.api.meta.Input;

import java.util.HashSet;
import java.util.Set;

public class LogoutResource implements LinksContainer, ActionsContainer {

    @Input(ignore = true)
    private final Set<Link> actions = new HashSet<>();

    @Input(ignore = true)
    private final Set<Link> links = new HashSet<>();

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
}
