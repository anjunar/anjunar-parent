package de.bitvale.common.ddd;

/**
 * @author Patrick Bittner on 02.05.2014.
 */
public interface Repository<V extends Key, A extends Entity> {

    A create();

    A read(V key);

    A update(A aggregate);

    void delete(V key);


}
