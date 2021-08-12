package de.bitvale.common.rest;

import de.bitvale.common.rest.api.Link;
import de.bitvale.common.security.Identity;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.ParamConverterProvider;
import java.io.Serializable;
import java.util.function.Consumer;

/**
 * @author Patrick Bittner on 11.06.2015.
 */
@Dependent
public class URLBuilderFactory implements Serializable {

    private final Identity identity;

    private final EntityManager entityManager;

    private final ParamConverterProvider converterProvider;

    @Inject
    public URLBuilderFactory(Identity identity, EntityManager entityManager, ParamConverterProvider converterProvider) {
        this.identity = identity;
        this.entityManager = entityManager;
        this.converterProvider = converterProvider;
    }

    public URLBuilderFactory() {
        this(null, null, null);
    }

    public <B> URLBuilder<B> from(Class<B> aClass) {
        return new URLBuilder<>(aClass, UriBuilder.fromPath("/"), identity, entityManager, converterProvider);
    }

    public <E> URLBuilder<E> from(Class<E> aCLass, Consumer<E> record, Consumer<Link> link) {
        URLBuilder<E> builder = new URLBuilder<>(aCLass, UriBuilder.fromPath("/"), identity, entityManager, converterProvider);
        builder.record(record);
        builder.build(link);
        return builder;
    }

}
