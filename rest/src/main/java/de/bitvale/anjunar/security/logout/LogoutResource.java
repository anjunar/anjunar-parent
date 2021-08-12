package de.bitvale.anjunar.security.logout;

import de.bitvale.anjunar.ApplicationResource;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.security.Identity;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

@Path("security/logout")
@ApplicationScoped
public class LogoutResource {

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public LogoutResource(Identity identity, URLBuilderFactory factory) {
        this.identity = identity;
        this.factory = factory;
    }

    public LogoutResource() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public LogoutForm logout1() {
        LogoutForm resource = new LogoutForm();

        factory.from(LogoutResource.class)
                .record(LogoutResource::logout)
                .build(resource::addAction);

        return resource;
    }

    @POST
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public LogoutForm logout() {

        identity.logout();

        LogoutForm resource = new LogoutForm();

        factory.from(ApplicationResource.class)
                .rel("redirect")
                .record(ApplicationResource::service)
                .build(resource::addLink);

        return resource;
    }

}
