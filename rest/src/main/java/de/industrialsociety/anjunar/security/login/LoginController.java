package de.industrialsociety.anjunar.security.login;

import de.industrialsociety.common.rest.api.meta.MetaForm;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.rest.Secured;
import de.industrialsociety.common.rest.api.Link;
import de.industrialsociety.common.security.User;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.UUID;

@Path("security")
@ApplicationScoped
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
    public MetaForm<LoginResource> login() {
        LoginResource loginResource = new LoginResource();

        loginResource.setFirstName("Patrick");
        loginResource.setLastName("Bittner");
        loginResource.setBirthdate(LocalDate.of(1980, 4, 1));
        loginResource.setPassword("patrick");

        loginResource.addAction(new Link("service/security/login", "POST", "login"));

        return new MetaForm<>(loginResource, identity.getLanguage());
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("login")
    public Response login(LoginResource resource) {

        User authenticate = identity.findUser(resource.getFirstName(), resource.getLastName(), resource.getBirthdate());

        if (authenticate.getPassword().equals(resource.getPassword())) {
            identity.authenticate(authenticate);

            resource.addLink(new Link("service/root", "GET", "redirect"));

            return Response.accepted(resource).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @POST
    @Secured
    @Path("runas")
    @RolesAllowed({"Administrator"})
    public Response runAs(@QueryParam("id") UUID id) {
        User authenticate = identity.findUser(id);

        identity.authenticate(authenticate);

        return Response.accepted().build();
    }

}
