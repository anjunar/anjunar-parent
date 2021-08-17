package de.bitvale.common.rest.api;

import de.bitvale.common.ddd.AbstractEntity;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;

public abstract class AbstractRestEntityConverter<E extends AbstractEntity, R extends AbstractRestEntity> {

    public R factory(R restEntity, E entity) {
        restEntity.setId(entity.getId());
        restEntity.setCreated(entity.getCreated());
        restEntity.setModified(entity.getModified());
        return restEntity;
    }

    public abstract E updater(R restEntity, E entity, EntityManager entityManager, Identity identity);

}
