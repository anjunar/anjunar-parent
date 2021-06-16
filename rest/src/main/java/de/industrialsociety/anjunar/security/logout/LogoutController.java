package de.industrialsociety.anjunar.security.logout;

import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.rest.api.Link;
import de.industrialsociety.common.rest.api.meta.MetaForm;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

@Path("security/logout")
@ApplicationScoped
public class LogoutController {

    private final Identity identity;

    @Inject
    public LogoutController(Identity identity) {
        this.identity = identity;
    }

    public LogoutController() {
        this(null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaForm<LogoutResource> logout1() {
        LogoutResource resource = new LogoutResource();

        MetaForm<LogoutResource> metaForm = new MetaForm<>(resource, identity.getLanguage());

        resource.addAction(new Link("service/security/logout", "POST", "logout" ));

        return metaForm;
    }

    @POST
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public LogoutResource logout() {
        identity.logout();

        LogoutResource resource = new LogoutResource();

        resource.addLink(new Link("service/root", "GET", "redirect"));

        return resource;
    }

}
