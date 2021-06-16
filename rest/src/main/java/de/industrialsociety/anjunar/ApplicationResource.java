package de.industrialsociety.anjunar;

import de.industrialsociety.common.rest.api.Link;
import de.industrialsociety.common.rest.api.LinksContainer;
import de.industrialsociety.anjunar.shared.users.user.UserResource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApplicationResource implements LinksContainer {

    private UserResource user;

    private List<ExtensionPointResource> extensionPoints;

    private Set<Link> links = new HashSet<>();

    public UserResource getUser() {
        return user;
    }

    public void setUser(UserResource user) {
        this.user = user;
    }

    public List<ExtensionPointResource> getExtensionPoints() {
        return extensionPoints;
    }

    public void setExtensionPoints(List<ExtensionPointResource> extensionPoints) {
        this.extensionPoints = extensionPoints;
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
