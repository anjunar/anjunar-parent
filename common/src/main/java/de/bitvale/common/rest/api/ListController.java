package de.bitvale.common.rest.api;

import javax.ws.rs.*;

public interface ListController<R extends AbstractRestEntity, S> {

    @POST
    @Produces("application/json")
    Container<R> list(@BeanParam S search);
}
