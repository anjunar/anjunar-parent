package de.bitvale.common.rest.api;

import javax.ws.rs.*;

public interface ListResource<R extends AbstractRestEntity, S> {

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    Container<R> list(S search);

}
