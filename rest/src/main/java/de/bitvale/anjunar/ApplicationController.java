package de.bitvale.anjunar;

import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.security.Identity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Locale;

@Path("/")
@ApplicationScoped
public class ApplicationController {

    private final Identity identity;

    @Inject
    public ApplicationController(Identity identity) {
        this.identity = identity;
    }

    public ApplicationController() {
        this(null);
    }

    @GET
    @Transactional
    @Path("language")
    public Response language(@QueryParam("lang") String locale) {

        identity.setLanguage(Locale.forLanguageTag(locale));

        return Response.ok().build();
    }

    @GET
    @Produces("application/json")
    @Transactional
    public UserResource service() {

        if (identity.isLoggedIn()) {

            UserResource userResource = UserResource.factory(identity.getUser());

            identity.createLink("security/logout", "POST", "logout", userResource::addLink);
            identity.createLink("home/timeline", "GET", "timeline", userResource::addLink);
            identity.createLink("control/users", "GET", "users", userResource::addLink);
            identity.createLink("pages/search", "GET", "search", userResource::addLink);
            identity.createLink("pages/page/create", "GET", "editor", userResource::addLink);
            identity.createLink("control/roles", "GET", "admin", userResource::addLink);
            if (identity.hasRole("Administrator")) {
                userResource.addLink(new Link("navigator/navigator", "GET", "navigator"));
            }

            return userResource;
        } else {
            UserResource userResource = new UserResource();
            userResource.setFirstName("Guest");
            userResource.setLastName("Guest");
            userResource.setLanguage(Locale.forLanguageTag("en-DE"));

            identity.createLink("security/login", "POST", "login", userResource::addLink);
            identity.createLink("security/register", "POST", "register", userResource::addLink);

            return userResource;
        }

    }

}
