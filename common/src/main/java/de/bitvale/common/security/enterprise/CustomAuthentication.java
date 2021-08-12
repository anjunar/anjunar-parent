package de.bitvale.common.security.enterprise;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.authentication.mechanism.http.RememberMe;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AutoApplySession
@ApplicationScoped
@RememberMe
public class CustomAuthentication implements HttpAuthenticationMechanism {

    private final IdentityStoreHandler identityStoreHandler;

    @Inject
    public CustomAuthentication(IdentityStoreHandler identityStoreHandler) {
        this.identityStoreHandler = identityStoreHandler;
    }

    public CustomAuthentication() {
        this(null);
    }

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {

        Object credential = request.getAttribute("credential");

        if (credential != null) {

            CivilCredential civilCredential = (CivilCredential) credential;

            CredentialValidationResult result = identityStoreHandler.validate(civilCredential);

            return httpMessageContext.notifyContainerAboutLogin(result);
        }

        if (httpMessageContext.isProtected()) {
            return httpMessageContext.responseUnauthorized();
        }

        return httpMessageContext.doNothing();
    }

    @Override
    public void cleanSubject(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        httpMessageContext.cleanClientSubject();
    }
}
