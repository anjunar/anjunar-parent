package de.bitvale.anjunar.control.users;

import de.bitvale.common.security.Identity;
import de.bitvale.common.ddd.AbstractCriteriaSearchService;
import de.bitvale.common.security.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class UsersService extends AbstractCriteriaSearchService<User, UsersSearch> {

    @Inject
    public UsersService(EntityManager entityManager, Identity identity) {
        super(entityManager, identity);
    }

    public UsersService() {
        this(null, null);
    }

}
