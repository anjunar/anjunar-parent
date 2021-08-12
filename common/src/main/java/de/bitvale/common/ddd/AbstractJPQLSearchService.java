package de.bitvale.common.ddd;

import com.google.common.reflect.TypeToken;
import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class AbstractJPQLSearchService<E, S extends AbstractRestSearch> {

    protected final EntityManager entityManager;

    protected final Identity identity;

    public AbstractJPQLSearchService(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public Class<E> getEntityClass() {
        return (Class<E>) TypeToken.of(getClass()).resolveType(AbstractJPQLSearchService.class.getTypeParameters()[0]).getRawType();
    }

    public Class<S> getSearchClass() {
        return (Class<S>) TypeToken.of(getClass()).resolveType(AbstractJPQLSearchService.class.getTypeParameters()[1]).getRawType();
    }

    public Predicate filter(CriteriaBuilder builder, Root<E> root, CriteriaQuery<?> query) {
        return entityManager.getCriteriaBuilder().conjunction();
    }

    abstract public List<E> find(S search);

    abstract public long count(S search);

}
