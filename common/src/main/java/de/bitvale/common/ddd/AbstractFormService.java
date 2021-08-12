package de.bitvale.common.ddd;

import com.google.common.reflect.TypeToken;

import javax.persistence.EntityManager;
import java.util.UUID;

public class AbstractFormService<E> {

    protected final EntityManager entityManager;

    public AbstractFormService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Class<E> getEntityClass() {
        return (Class<E>) TypeToken.of(getClass()).resolveType(AbstractFormService.class.getTypeParameters()[0]).getRawType();
    }

    public E find(UUID id) {
        return entityManager.find(getEntityClass(), id);
    }

    public void save(E entity) {
        entityManager.persist(entity);
    }

    public E update(E entity) {
       return entityManager.merge(entity);
    }

    public void delete(UUID id) {
        E entity = find(id);
        entityManager.remove(entity);
    }


}
