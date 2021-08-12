package de.bitvale.common.ddd;

import java.util.UUID;

/**
 * @author Patrick Bittner on 02.05.2014.
 */
public interface Aggregate {

    UUID getId();

    int getVersion();

}
