package de.bitvale.common.rest;


import de.bitvale.common.security.Identity;
import org.jboss.resteasy.core.interception.PostMatchContainerRequestContext;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Patrick Bittner on 08.06.2015.
 */
@Secured
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    private final Identity identity;

    @Inject
    public SecurityFilter(Identity identity) {
        this.identity = identity;
    }

    public SecurityFilter() {
        this(null);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (identity.isLoggedIn()) {
            PostMatchContainerRequestContext postMatchContainerRequestContext = (PostMatchContainerRequestContext) requestContext;
            RolesAllowed rolesAllowed = postMatchContainerRequestContext.getResourceMethod().getMethod().getAnnotation(RolesAllowed.class);
            if (rolesAllowed != null) {
                if (! hasRole(rolesAllowed)) {
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                }
            }
        } else {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    private boolean hasRole(RolesAllowed rolesAllowed) {
        for (String role : rolesAllowed.value()) {
            if (identity.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

}
