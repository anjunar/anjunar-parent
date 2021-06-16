package de.industrialsociety.common.rest.api;

import de.industrialsociety.common.rest.api.meta.MetaForm;

import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

public interface ListFormController<R extends AbstractRestEntity, S> {

    @POST
    @Produces("application/json")
    Container<MetaForm<R>> list(@BeanParam S search);
}
