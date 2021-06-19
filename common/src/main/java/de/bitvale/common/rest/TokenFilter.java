package de.bitvale.common.rest;

/*
import net.portrix.generic.security.Identity;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.credential.TokenCredential;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(1)
public class TokenFilter implements ContainerRequestFilter {

    private final Identity identity;

    private final DefaultLoginCredentials credentials;

    @Inject
    public TokenFilter(Identity identity,
                       DefaultLoginCredentials credentials) {
        this.identity = identity;
        this.credentials = credentials;
    }

    public TokenFilter() {
        this(null, null);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if (!identity.isLoggedIn()) {
            Cookie rememberMe = requestContext.getCookies().get("rememberMe");
            if (rememberMe != null) {
                String[] split = rememberMe.getValue().split("\\.");
                credentials.setCredential(new TokenCredential(new LoginToken(split[0], split[1])));
                identity.login();
            }
        }

    }

}
*/
