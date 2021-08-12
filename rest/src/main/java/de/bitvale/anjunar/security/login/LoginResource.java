package de.bitvale.anjunar.security.login;

import de.bitvale.anjunar.ApplicationResource;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("security")
@RequestScoped
public class LoginResource {

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public LoginResource(Identity identity, URLBuilderFactory factory) {
        this.identity = identity;
        this.factory = factory;
    }

    public LoginResource() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    @Path("login")
    public LoginForm login() {
        LoginForm loginForm = new LoginForm();

        factory.from(LoginResource.class)
                .record(loginResource -> loginResource.login(new LoginForm()))
                .build(loginForm::addAction);

        return loginForm;
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("login")
    public Response login(@Valid LoginForm resource) {

        User user = identity.findUser(resource.getFirstname(), resource.getLastname(), resource.getBirthday());

        if (identity.authenticate(user)) {
            factory.from(ApplicationResource.class)
                    .rel("redirect")
                    .record(ApplicationResource::service)
                    .build(resource::addLink);

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
