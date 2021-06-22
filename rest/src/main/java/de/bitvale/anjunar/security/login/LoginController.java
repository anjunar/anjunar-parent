package de.bitvale.anjunar.security.login;

import de.bitvale.common.rest.api.Link;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.UUID;

@Path("security")
@RequestScoped
public class LoginController {

    private final Identity identity;

    @Inject
    public LoginController(Identity identity) {
        this.identity = identity;
    }

    public LoginController() {
        this(null);
    }

    @GET
    @Produces("application/json")
    @Path("login")
    public LoginResource login() {
        LoginResource loginResource = new LoginResource();

        loginResource.addAction(new Link("service/security/login", "POST", "login"));

        return loginResource;
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("login")
    public Response login(@Valid LoginResource resource) {

        User user = identity.findUser(resource.getFirstName(), resource.getLastName(), resource.getBirthdate());

        if (identity.authenticate(user)) {
            resource.addLink(new Link("service/root", "GET", "redirect"));

            return Response.accepted(resource).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @POST
    @Path("runas")
    @RolesAllowed({"Administrator"})
    public Response runAs(@QueryParam("id") UUID id) {
        User authenticate = identity.findUser(id);

        identity.authenticate(authenticate);

        return Response.accepted().build();
    }

}
