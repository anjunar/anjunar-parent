package de.bitvale.common.rest.api;

import javax.validation.Valid;
import javax.ws.rs.*;
import java.util.UUID;

public interface FormResource<F extends AbstractRestEntity> {

    @Produces("application/json")
    @GET
    F read(@QueryParam("id") UUID id);

    @Consumes("application/json")
    @Produces("application/json")
    @POST
    F save(@Valid F form);

    @Consumes("application/json")
    @Produces("application/json")
    @PUT
    F update(@QueryParam("id") UUID id,@Valid F form);

    @DELETE
    void delete(@QueryParam("id") UUID id);
}
