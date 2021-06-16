package de.industrialsociety.anjunar.control.users.user;

import de.industrialsociety.common.rest.api.meta.MetaForm;
import de.industrialsociety.common.filedisk.Base64Resource;
import de.industrialsociety.common.filedisk.FileDiskUtils;
import de.industrialsociety.common.rest.Secured;
import de.industrialsociety.common.rest.api.Blob;
import de.industrialsociety.common.rest.api.FormController;
import de.industrialsociety.common.security.*;
import de.industrialsociety.anjunar.control.roles.role.RoleResource;
import de.industrialsociety.common.security.*;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Secured
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
    public MetaForm<UserResource> create() {
        UserResource resource = new UserResource();

        Blob picture = new Blob();

        picture.setData("");

        resource.setPicture(picture);

        identity.createLink("control/users/user", "POST", "save", resource::addAction);

        MetaForm<UserResource> metaForm = new MetaForm<>(resource, identity.getLanguage());

        identity.createLink("control/users/user/validate", "POST", "validate", metaForm::addSource);
        identity.createLink("control/roles", "POST", "roles", metaForm::addSource);


        return metaForm;
    }

    @Transactional
    @Produces("application/json")
    @GET
    @Path("current")
    @RolesAllowed({"Administrator", "User"})
    public UserResource current() {

        User user = entityManager.find(User.class, identity.getUser().getId());

        Set<Relationship> relationships = user.getRelationships();
        Set<RoleResource> roles = new HashSet<>();
        for (Relationship relationship : relationships) {
            Role group = relationship.getGroup();
            RoleResource resource = new RoleResource();
            resource.setId(group.getId());
            resource.setName(group.getName());
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
    public MetaForm<UserResource> read(UUID id) {

        User user = entityManager.find(User.class, id);

        Set<Relationship> relationships = user.getRelationships();
        Set<RoleResource> roles = new HashSet<>();
        for (Relationship relationship : relationships) {
            Role group = relationship.getGroup();
            RoleResource resource = new RoleResource();
            resource.setId(group.getId());
            resource.setName(group.getName());
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

        MetaForm<UserResource> metaForm = new MetaForm<>(resource, identity.getLanguage());

        identity.createLink("control/users/user/validate", "POST", "validate", metaForm::addSource);
        identity.createLink("control/roles", "POST", "roles", metaForm::addSource);


        return metaForm;
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
        user.getRelationships().clear();
        for (RoleResource roleResource : roleResources) {
            Role role = entityManager.find(Role.class, roleResource.getId());
            Relationship relationship = new Relationship();
            relationship.setGroup(role);
            relationship.setUser(user);
            user.getRelationships().add(relationship);
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
        user.getRelationships().clear();
        for (RoleResource roleResource : roleResources) {
            Role role = entityManager.find(Role.class, roleResource.getId());
            Relationship relationship = new Relationship();
            relationship.setGroup(role);
            relationship.setUser(user);
            user.getRelationships().add(relationship);
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
