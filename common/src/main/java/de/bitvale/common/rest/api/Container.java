package de.bitvale.common.rest.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author by Patrick Bittner on 12.06.15.
 */
public class Container<R> implements LinksContainer {

    private final List<R> rows;

    private final long size;

    private Set<Link> links = new HashSet<>();

    public Container(List<R> rows, long size) {
        this.rows = rows;
        this.size = size;
    }

    public List<R> getRows() {
        return rows;
    }

    public long getSize() {
        return size;
    }

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    @Override
    public boolean addLink(Link link) {
        return links.add(link);
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }
}
