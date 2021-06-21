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

        Blob picture = new Blob();

        picture.setData("");

        resource.setPicture(picture);

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

        Set<Role> relationships = user.getRoles();
        Set<RoleResource> roles = new HashSet<>();
        for (Role role : relationships) {
            RoleResource resource = new RoleResource();
            resource.setId(role.getId());
            resource.setName(role.getName());
            roles.add(resource);
        }

        UserResource resource = new UserResource();
        resource.setId(user.getId());
        resource.setFirstName(user.getFirstName());
        resource.setLastName(user.getLastName());
        resource.setBirthdate(user.getBirthDate());
        resource.setPassword(user.getPassword());
        resource.setEnabled(user.isEnabled());
        resource.setRoles(roles);
        resource.setEmail(user.getEmail());

        Blob picture = new Blob();
        if (user.getPicture() != null) {
            picture.setData(FileDiskUtils.buildBase64(user.getPicture().getType(), user.getPicture().getSubType(), user.getPicture().getData()));
            picture.setName(user.getPicture().getName());
            picture.setLastModified(user.getPicture().getLastModified());
        }
        resource.setPicture(picture);

        identity.createLink("control/users/user?id=" + user.getId(), "PUT", "update", resource::addAction);
        identity.createLink("control/users/user?id=" + user.getId(), "DELETE", "delete", resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public UserResource read(UUID id) {

        User user = entityManager.find(User.class, id);

        Set<Role> relationships = user.getRoles();
        Set<RoleResource> roles = new HashSet<>();
        for (Role role : relationships) {
            RoleResource resource = new RoleResource();
            resource.setId(role.getId());
            resource.setName(role.getName());
            roles.add(resource);
        }

        UserResource resource = new UserResource();
        resource.setId(user.getId());
        resource.setFirstName(user.getFirstName());
        resource.setLastName(user.getLastName());
        resource.setBirthdate(user.getBirthDate());
        resource.setPassword(user.getPassword());
        resource.setEnabled(user.isEnabled());
        resource.setRoles(roles);
        resource.setEmail(user.getEmail());

        Blob picture = new Blob();
        if (user.getPicture() != null) {
            picture.setData(FileDiskUtils.buildBase64(user.getPicture().getType(), user.getPicture().getSubType(), user.getPicture().getData()));
            picture.setName(user.getPicture().getName());
            picture.setLastModified(user.getPicture().getLastModified());
        }
        resource.setPicture(picture);

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

        user.setFirstName(resource.getFirstName());
        user.setLastName(resource.getLastName());
        user.setBirthDate(resource.getBirthdate());
        user.setPassword(resource.getPassword());
        user.setEnabled(true);
        user.setEmail(resource.getEmail());

        if (resource.getPicture().getData() != null) {
            Base64Resource process = FileDiskUtils.extractBase64(resource.getPicture().getData());

            UserImage picture = new UserImage();
            picture.setData(process.getData());
            picture.setType(process.getType());
            picture.setSubType(process.getSubType());
            picture.setName(resource.getPicture().getName());
            picture.setLastModified(resource.getPicture().getLastModified());
            user.setPicture(picture);
        }

        Set<RoleResource> roleResources = resource.getRoles();
        user.getRoles().clear();
        for (RoleResource roleResource : roleResources) {
            Role role = entityManager.find(Role.class, roleResource.getId());
            user.getRoles().add(role);
        }


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

        user.setFirstName(resource.getFirstName());
        user.setLastName(resource.getLastName());
        user.setBirthDate(resource.getBirthdate());
        user.setPassword(resource.getPassword());
        user.setEnabled(resource.isEnabled());
        user.setEmail(resource.getEmail());

        if (resource.getPicture().getData() != null) {
            Base64Resource process = FileDiskUtils.extractBase64(resource.getPicture().getData());
            UserImage picture = new UserImage();
            picture.setName(resource.getPicture().getName());
            picture.setData(process.getData());
            picture.setType(process.getType());
            picture.setSubType(process.getSubType());
            picture.setLastModified(resource.getPicture().getLastModified());
            user.setPicture(picture);
        }

        Set<RoleResource> roleResources = resource.getRoles();
        user.getRoles().clear();
        for (RoleResource roleResource : roleResources) {
            Role role = entityManager.find(Role.class, roleResource.getId());
            user.getRoles().add(role);
        }

        if (identity.isLoggedIn()) {
            identity.createLink("control/users/user?id=" + user.getId(), "PUT", "update", resource::addAction);
            identity.createLink("control/users/user?id=" + user.getId(), "DELETE", "delete", resource::addAction);
        }

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
