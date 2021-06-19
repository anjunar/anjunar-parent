package de.bitvale.common.rest.api;

import java.util.Set;

public interface ActionsContainer {

    Set<Link> getActions();

    boolean addAction(Link link);

}
