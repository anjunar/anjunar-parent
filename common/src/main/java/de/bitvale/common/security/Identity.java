package de.bitvale.common.security;

import de.bitvale.common.rest.api.Link;
import de.bitvale.common.security.enterprise.Authenticator;
import de.bitvale.common.security.enterprise.CivilCredential;
import de.bitvale.jsr339.Operation;
import de.bitvale.jsr339.Resource;
import de.bitvale.jsr339.cdi.JaxRSExtension;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.credential.Password;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@SessionScoped
public class Identity implements Serializable {

    private final IdentityService service;

    private final JaxRSExtension extension;

    private final Authenticator authenticator;

    private Locale language = Locale.ENGLISH;

    @Inject
    public Identity(IdentityService service, JaxRSExtension extension, Authenticator authenticator) {
        this.service = service;
        this.extension = extension;
        this.authenticator = authenticator;
    }

    public Identity() {
        this(null, null, null);
    }

    public boolean authenticate(User user) {
        CivilCredential credential = new CivilCredential(user.getFirstName(), user.getLastName(), user.getBirthDate(), new Password(user.getPassword()));

        AuthenticationStatus authenticate = authenticator.authenticate(credential);

        return authenticate == AuthenticationStatus.SUCCESS;
    }

    public void logout() {
        authenticator.logout();
    }

    public boolean isLoggedIn() {
        return authenticator.getUserPrincipal() != null;
    }

    public User getUser() {
        return findUser(UUID.fromString(authenticator.getUserPrincipal().getName()));
    }

    public User findUser(UUID id) {
        return service.findUser(id);
    }

    public User findUser(String firstName, String lastName, LocalDate birthDate) {
        return service.findUser(firstName, lastName, birthDate);
    }

    public User findUser(String email) {
        return service.findUser(email);
    }

    @Transactional
    public boolean hasRole(String role) {
        User user = service.findUser(getUser().getId());
        Set<Role> roles = user.getRoles();
        for (Role group : roles) {
            if (group.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean hasPermission(String value, String method) {
        if (isLoggedIn()) {
            User user = service.findUser(getUser().getId());
            Set<Role> relationships = user.getRoles();
            for (Role role : relationships) {
                Set<Permission> permissions = role.getPermissions();
                for (Permission permission : permissions) {
                    if (permission.getUrl().contains(value) && permission.getMethod().equals(method)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Transactional
    public void createLink(String url, String method, String rel, Consumer<Link> loader) {
        String[] rawUrl = url.split("\\?");

        Set<Resource<?>> resources = extension.getResources();
        for (Resource<?> resource : resources) {
            for (Operation operation : resource.getOperations()) {
                if (operation.getUrl().equals(rawUrl[0]) && operation.getHttpMethod().equals(method)) {
                    RolesAllowed rolesAllowed = operation.getMethod().getAnnotation(RolesAllowed.class);
                    if (rolesAllowed != null) {
                        for (String role : rolesAllowed.value()) {
                            if (hasRole(role)) {
                                Link link = new Link("service/" + url, method, rel);
                                loader.accept(link);
                            }
                        }
                    } else {
                        Link link = new Link("service/" + url, method, rel);
                        loader.accept(link);
                    }
                }
            }
        }
    }

    public Locale getLanguage() {
        if (isLoggedIn()) {
            if (getUser().getLanguage() == null) {
                return language;
            }
            return getUser().getLanguage();
        }
        return language;
    }

    public void setLanguage(Locale language) {
        if (isLoggedIn()) {
            getUser().setLanguage(language);
        }
        this.language = language;
    }

    public User findUserByToken(String token) {
        return service.findUserByToken(token);
    }
}
