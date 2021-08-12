package de.bitvale.common.ddd;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Patrick Bittner on 02.05.2014.
 */
public interface Key extends Serializable {

    UUID getId();

    Class<?> getType();

}
