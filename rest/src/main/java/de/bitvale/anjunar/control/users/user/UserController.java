package de.bitvale.anjunar.control.users.user;

import de.bitvale.anjunar.control.roles.role.RoleResource;
import de.bitvale.common.filedisk.Base64Resource;
import de.bitvale.common.filedisk.FileDiskUtils;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.FormController;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Role;
import de.bitvale.common.security.User;
import de.bitvale.common.security.UserImage;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
@Path("control/users/user")
public class UserController implements FormController<UserResource> {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public UserController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public UserController() {
        this(null, null);
    }

    @Path("validate")
    @POST
    @RolesAllowed("Administrator")
    public Response validate(UserResource resource) {

        User user;
        try {
            user = entityManager.createQuery("select u from User u where u.firstName = :firstName and u.lastName = :lastName and u.birthDate = :birthDate", User.class)
                    .setParameter("firstName", resource.getFirstName())
                    .setParameter("lastName", resource.getLastName())
                    .setParameter("birthDate", resource.getBirthdate())
                    .getSingleResult();
        } catch (NoResultException e) {
            user = null;
        }

        if (resource.getId() == null) {
            if (user == null) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            if (user == null) {
                return Response.ok().build();
            } else {
                if (user.getId().equals(resource.getId())) {
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }
        }
    }

    @Transactional
    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed("Administrator")
    public UserResource create() {
        UserResource resource = new UserResource();

        identity.createLink("control/users/user", "POST", "save", resource::addAction);
        identity.createLink("control/users/user/validate", "POST", "validate", resource::addSource);
        identity.createLink("control/roles", "POST", "roles", resource::addSource);


        return resource;
    }

    @Transactional
    @Produces("application/json")
    @GET
    @Path("current")
    @RolesAllowed({"Administrator", "User"})
    public UserResource current() {

        User user = entityManager.find(User.class, identity.getUser().getId());

        UserResource resource = UserResource.factory(user);

        identity.createLink("control/users/user?id=" + user.getId(), "PUT", "update", resource::addAction);
        identity.createLink("control/users/user?id=" + user.getId(), "DELETE", "delete", resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public UserResource read(UUID id) {

        User user = entityManager.find(User.class, id);

        UserResource resource = UserResource.factory(user);

        identity.createLink("control/users/user?id=" + user.getId(), "PUT", "update", resource::addAction);
        identity.createLink("control/users/user?id=" + user.getId(), "DELETE", "delete", resource::addAction);
        identity.createLink("security/runas?id=" + user.getId(), "POST", "runas", resource::addAction);

        identity.createLink("control/users/user/validate", "POST", "validate", resource::addSource);
        identity.createLink("control/roles", "POST", "roles", resource::addSource);


        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public UserResource save(UserResource resource) {

        User user = new User();

        UserResource.updater(resource, user, entityManager);

        entityManager.persist(user);

        resource.setId(user.getId());

        identity.createLink("control/users/user?id=" + user.getId(), "PUT", "update", resource::addAction);
        identity.createLink("control/users/user?id=" + user.getId(), "DELETE", "delete", resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public UserResource update(UUID id, UserResource resource) {
        User user = entityManager.find(User.class, id);

        UserResource.updater(resource, user, entityManager);

        identity.createLink("control/users/user?id=" + user.getId(), "PUT", "update", resource::addAction);
        identity.createLink("control/users/user?id=" + user.getId(), "DELETE", "delete", resource::addAction);

        return resource;

    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public void delete(UUID id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }
}
