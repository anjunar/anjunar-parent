package de.bitvale.common.ddd;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.UUID;

@ApplicationScoped
public class FormService {

    private final EntityManager entityManager;

    @Inject
    public FormService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public FormService() {
        this(null);
    }

    public <E> E find(Class<E> entityClass, UUID id) {
        return entityManager.find(entityClass, id);
    }

    public <E> void save(E entity) {
        entityManager.persist(entity);
    }

    public <E> E update(E entity) {
        return entityManager.merge(entity);
    }

    public <E> void delete(Class<E> entityClass, UUID id) {
        E entity = find(entityClass, id);
        entityManager.remove(entity);
    }


}
