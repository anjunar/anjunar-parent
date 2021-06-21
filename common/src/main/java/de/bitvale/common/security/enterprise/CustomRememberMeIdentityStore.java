package de.bitvale.common.security.enterprise;

import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Role;
import de.bitvale.common.security.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.RememberMeIdentityStore;
import javax.transaction.Transactional;
import java.security.Key;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class CustomRememberMeIdentityStore implements RememberMeIdentityStore {

    private final Identity identity;

    @Inject
    public CustomRememberMeIdentityStore(Identity identity) {
        this.identity = identity;
    }

    public CustomRememberMeIdentityStore() {
        this(null);
    }

    @Override
    @Transactional
    public CredentialValidationResult validate(RememberMeCredential credential) {
        User user = identity.findUserByToken(credential.getToken());

        if (user == null) {
            return CredentialValidationResult.INVALID_RESULT;
        }

        Set<Role> userRoles = user.getRoles();
        Set<String> roles = new HashSet<>();
        for (Role role : userRoles) {
            roles.add(role.getName());
        }

        return new CredentialValidationResult(user.getId().toString(), roles);
    }

    @Override
    @Transactional
    public String generateLoginToken(CallerPrincipal callerPrincipal, Set<String> groups) {
        User user = identity.findUser(UUID.fromString(callerPrincipal.getName()));
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String compact = Jwts.builder().setSubject(callerPrincipal.getName()).signWith(key).compact();
        user.setToken(compact);
        return compact;
    }

    @Override
    @Transactional
    public void removeLoginToken(String token) {
        User user = identity.findUserByToken(token);
        user.setToken("");
    }
}
