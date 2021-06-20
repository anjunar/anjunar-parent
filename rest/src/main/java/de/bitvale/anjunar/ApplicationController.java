package de.bitvale.anjunar;

import com.google.common.collect.Sets;
import de.bitvale.common.security.Identity;
import de.bitvale.common.rest.api.Link;
import de.bitvale.anjunar.shared.users.user.UserResource;
import jakarta.servlet.ServletContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Path("/root")
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
    @Produces("application/json")
    @Transactional
    public ApplicationResource service(@QueryParam("lang") Locale locale) {

        if (locale != null) {
            identity.setLanguage(locale);
        }

        if (identity.isLoggedIn()) {
            ApplicationResource resource = new ApplicationResource();

            UserResource userResource = new UserResource();
            userResource.setId(identity.getUser().getId());
            userResource.setFirstName(identity.getUser().getFirstName());
            userResource.setLastName(identity.getUser().getLastName());
            userResource.setBirthDate(identity.getUser().getBirthDate());
            userResource.setLanguage(identity.getUser().getLanguage());
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
