package de.industrialsociety.anjunar.control.roles;

import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.ddd.AbstractCriteriaSearchService;
import de.industrialsociety.common.security.Role;

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
