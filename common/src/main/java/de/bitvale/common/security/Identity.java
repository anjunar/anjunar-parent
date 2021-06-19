package de.bitvale.common.security;

import de.bitvale.common.rest.api.Link;
import de.bitvale.jsr339.Operation;
import de.bitvale.jsr339.Resource;
import de.bitvale.jsr339.cdi.JaxRSExtension;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
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

    private UUID userId;

    private Locale language = Locale.ENGLISH;

    @Inject
    public Identity(IdentityService service, JaxRSExtension extension) {
        this.service = service;
        this.extension = extension;
    }

    public Identity() {
        this(null, null);
    }

    public boolean authenticate(User user) {
        boolean authenticated = service.authenticate(user);
        if (authenticated) {
            this.userId = user.getId();
        }
        return authenticated;
    }

    public void logout() {
        userId = null;
    }

    public boolean isLoggedIn() {
        return userId != null;
    }

    public User getUser() {
        return findUser(userId);
    }

    public User findUser(UUID id) {
        return service.findUser(id);
    }

    public User findUser(String firstName, String lastName, LocalDate birthDate) {
        return service.findUser(firstName, lastName, birthDate);
    }

    @Transactional
    public boolean hasRole(String role) {
        User user = service.findUser(getUser().getId());
        Set<Relationship> relationships = user.getRelationships();
        for (Relationship relationship : relationships) {
            Role group = relationship.getGroup();
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
            Set<Relationship> relationships = user.getRelationships();
            for (Relationship relationship : relationships) {
                Role group = relationship.getGroup();
                Set<Permission> permissions = group.getPermissions();
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
}
