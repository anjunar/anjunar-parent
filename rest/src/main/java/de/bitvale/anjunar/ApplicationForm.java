package de.bitvale.anjunar;

import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.LinksContainer;
import de.bitvale.anjunar.shared.users.user.UserSelect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApplicationForm implements LinksContainer {

    private UserSelect user;

    private Set<Link> links = new HashSet<>();

    public UserSelect getUser() {
        return user;
    }

    public void setUser(UserSelect user) {
        this.user = user;
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
