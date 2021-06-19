package de.bitvale.common.rest.api;

import de.bitvale.common.rest.api.meta.MetaForm;

import javax.validation.Valid;
import javax.ws.rs.*;
import java.util.UUID;

public interface FormController<F extends AbstractRestEntity> {

    @Produces("application/json")
    @GET
    MetaForm<F> read(@QueryParam("id") UUID id);

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
