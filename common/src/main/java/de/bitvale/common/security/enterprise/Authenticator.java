package de.bitvale.common.security.enterprise;

import org.apache.deltaspike.core.api.common.DeltaSpike;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.Credential;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RequestScoped
public class Authenticator {

    private final SecurityContext securityContext;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    @Inject
    public Authenticator(SecurityContext securityContext, HttpServletRequest request,  @DeltaSpike HttpServletResponse response) {
        this.securityContext = securityContext;
        this.request = request;
        this.response = response;
    }

    public Authenticator() {
        this(null, null, null);
    }

    public AuthenticationStatus authenticate(Credential credential) {
        AuthenticationParameters parameters = new AuthenticationParameters();

        parameters.setCredential(credential);
        parameters.setNewAuthentication(true);
        parameters.setRememberMe(true);

        request.setAttribute("credential", credential);

        return securityContext.authenticate(request, response, parameters);
    }

    public void logout() {
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }
}
