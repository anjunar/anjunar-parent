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

        if (locale != null) {
            identity.setLanguage(Locale.forLanguageTag(locale));
        }

        return Response.ok().build();
    }

    @GET
    @Produces("application/json")
    @Transactional
    public ApplicationResource service() {

        if (identity.isLoggedIn()) {
            ApplicationResource resource = new ApplicationResource();

            UserResource userResource = new UserResource();
            userResource.setId(identity.getUser().getId());
            userResource.setFirstName(identity.getUser().getFirstName());
            userResource.setLastName(identity.getUser().getLastName());
            userResource.setBirthDate(identity.getUser().getBirthDate());
            userResource.setLanguage(identity.getLanguage());
            resource.setUser(userResource);

            Link logout = new Link("service/security/logout", "POST", "logout");
            resource.addLink(logout);

            identity.createLink("home/timeline", "GET", "timeline", resource::addLink);
            identity.createLink("control/users", "GET", "users", resource::addLink);
            identity.createLink("pages/search", "GET", "search", resource::addLink);
            identity.createLink("pages/page/create", "GET", "editor", resource::addLink);
            identity.createLink("control/roles", "GET", "admin", resource::addLink);
            if (identity.hasRole("Administrator")) {
                resource.addLink(new Link("navigator/navigator", "GET", "navigator"));
            }

            return resource;
        } else {
            ApplicationResource resource = new ApplicationResource();

            UserResource userResource = new UserResource();
            userResource.setFirstName("Guest");
            userResource.setLastName("Guest");
            resource.setUser(userResource);

            Link login = new Link("service/security/login", "POST", "login");
            resource.addLink(login);
            Link register = new Link("service/security/register", "POST", "register");
            resource.addLink(register);

            return resource;
        }

    }

}
