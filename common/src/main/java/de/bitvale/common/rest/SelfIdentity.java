package de.bitvale.common.rest;

import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import java.util.UUID;

public class SelfIdentity {

        private final Identity identity;

        private final EntityManager entityManager;

    public SelfIdentity(Identity identity, EntityManager entityManager) {
        this.identity = identity;
        this.entityManager = entityManager;
    }

    public boolean apply(UUID id) {
        if (! identity.hasRole("Administrator")) {
            return id.equals(identity.getUser().getId());
        }
        return true;
    }
}
