package de.bitvale.common.security.enterprise;

import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Role;
import de.bitvale.common.security.User;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class CustomIdentityStore implements IdentityStore {

    private final Identity identity;

    private final Event<LoggedInEvent> event;

    @Inject
    public CustomIdentityStore(Identity identity, Event<LoggedInEvent> event) {
        this.identity = identity;
        this.event = event;
    }

    public CustomIdentityStore() {
        this(null, null);
    }

    @Override
    @Transactional
    public CredentialValidationResult validate(Credential credential) {
        CivilCredential civilCredential = (CivilCredential) credential;

        User user = identity.findUser(civilCredential.getFirstName(), civilCredential.getLastName(), civilCredential.getBirthdate());

        if (user != null) {
            if (user.getPassword().equals(civilCredential.getPasswordAsString())) {
                event.fire(new LoggedInEvent(user));

                Set<Role> relationships = user.getRoles();
                Set<String> roles = new HashSet<>();
                for (Role role : relationships) {
                    roles.add(role.getName());
                }
                return new CredentialValidationResult(user.getId().toString(), roles);
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }

}
