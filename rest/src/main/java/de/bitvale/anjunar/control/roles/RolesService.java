package de.bitvale.anjunar.control.roles;

import de.bitvale.common.security.Identity;
import de.bitvale.common.ddd.AbstractCriteriaSearchService;
import de.bitvale.common.security.Role;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class RolesService extends AbstractCriteriaSearchService<Role, RolesSearch> {

    @Inject
    public RolesService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public RolesService() {
        this(null, null);
    }

}
