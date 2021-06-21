package de.bitvale.anjunar.security.logout;

import de.bitvale.common.security.Identity;
import de.bitvale.common.rest.api.Link;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

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
    public LogoutResource logout1() {
        LogoutResource resource = new LogoutResource();

        resource.addAction(new Link("service/security/logout", "POST", "logout" ));

        return resource;
    }

    @POST
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public LogoutResource logout(@Context HttpServletRequest request) {

        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        identity.logout();

        LogoutResource resource = new LogoutResource();

        resource.addLink(new Link("service/root", "GET", "redirect"));

        return resource;
    }

}
