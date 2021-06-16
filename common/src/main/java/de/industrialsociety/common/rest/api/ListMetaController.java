package de.industrialsociety.common.rest.api;

import de.industrialsociety.common.rest.api.meta.MetaTable;

import javax.ws.rs.*;

public interface ListMetaController<R extends AbstractRestEntity, S> {

    @GET
    @Produces("application/json")
    MetaTable<R> list();

    @POST
    @Produces("application/json")
    Container<R> list(@BeanParam S search);
}
