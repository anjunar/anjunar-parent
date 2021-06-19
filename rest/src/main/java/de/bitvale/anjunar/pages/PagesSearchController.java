package de.bitvale.anjunar.pages;

import de.bitvale.common.rest.Secured;
import de.bitvale.common.rest.api.meta.MetaForm;
import de.bitvale.common.security.Identity;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

@ApplicationScoped
@Secured
@Path("pages/search")
public class PagesSearchController {

    private final Identity identity;

    @Inject
    public PagesSearchController(Identity identity) {
        this.identity = identity;
    }

    public PagesSearchController() {
        this(null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaForm<PagesResource> create() {
        PagesResource resource = new PagesResource();

        identity.createLink("pages/like", "PUT", "like", resource::addAction);
        identity.createLink("pages/word", "PUT", "word", resource::addAction);

        return new MetaForm<>(resource, identity.getLanguage());
    }

}
