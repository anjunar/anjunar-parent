package de.bitvale.anjunar.control.roles.role;

import de.bitvale.common.rest.api.FormController;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Role;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.UUID;

@ApplicationScoped
@Path("control/roles/role")
public class RoleController implements FormController<RoleResource> {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public RoleController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public RoleController() {
        this(null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed("Administrator")
    public RoleResource create() {
        RoleResource resource = new RoleResource();

        identity.createLink("control/roles/role", "POST", "save", resource::addLink);

        return resource;
    }

    @Override
    @RolesAllowed({"Administrator", "User"})
    public RoleResource read(UUID id) {

        Role role = entityManager.find(Role.class, id);

        RoleResource resource = RoleResource.factory(role);

        identity.createLink("control/permissions?role=" + role.getId(), "GET", "permissions", resource::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public RoleResource save(@Valid RoleResource form) {

        Role role = new Role();

        RoleResource.updater(form, role);

        entityManager.persist(role);

        form.setId(role.getId());

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public RoleResource update(UUID id, @Valid RoleResource form) {

        Role role = entityManager.find(Role.class, id);

        RoleResource.updater(form, role);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public void delete(UUID id) {
        Role role = entityManager.find(Role.class, id);
        entityManager.remove(role);
    }
}
