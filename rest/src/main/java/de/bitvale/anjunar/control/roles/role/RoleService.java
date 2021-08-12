package de.bitvale.anjunar.control.roles.role;

import de.bitvale.common.security.Role;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class RoleService {

    private final EntityManager entityManager;

    @Inject
    public RoleService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public RoleService() {
        this(null);
    }

    @Transactional
    public void saveRole(Object entity) {
        entityManager.persist(entity);
    }

    @Transactional
    public Role updateRole(Role entity) {
        return entityManager.merge(entity);
    }

    @Transactional
    public void delete(Object entity) {
        entityManager.remove(entity);
    }

    public Role findRole(Object primaryKey) {
        return entityManager.find(Role.class, primaryKey);
    }
}
