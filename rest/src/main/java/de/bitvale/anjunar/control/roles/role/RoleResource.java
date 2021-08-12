package de.bitvale.anjunar.control.roles.role;

import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.FormResource;
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
public class RoleResource implements FormResource<RoleForm> {

    private final EntityManager entityManager;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public RoleResource(EntityManager entityManager, Identity identity, URLBuilderFactory factory) {
        this.entityManager = entityManager;
        this.identity = identity;
        this.factory = factory;
    }

    public RoleResource() {
        this(null, null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed("Administrator")
    public RoleForm create() {
        RoleForm resource = new RoleForm();

        factory.from(RoleResource.class)
                .record(roleResource -> roleResource.save(new RoleForm()))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @RolesAllowed({"Administrator", "User"})
    public RoleForm read(UUID id) {

        Role role = entityManager.find(Role.class, id);

        RoleForm resource = RoleForm.factory(role);

        factory.from(RoleResource.class)
                .record(roleResource -> roleResource.update(role.getId(), new RoleForm()))
                .build(resource::addAction);

        factory.from(RoleResource.class)
                .record(roleResource -> roleResource.delete(role.getId()))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public RoleForm save(@Valid RoleForm form) {

        Role role = new Role();

        RoleForm.updater(form, role);

        entityManager.persist(role);
        form.setId(role.getId());

        factory.from(RoleResource.class)
                .record(roleResource -> roleResource.update(role.getId(), new RoleForm()))
                .build(form::addAction);

        factory.from(RoleResource.class)
                .record(roleResource -> roleResource.delete(role.getId()))
                .build(form::addAction);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public RoleForm update(UUID id, @Valid RoleForm form) {

        Role role = entityManager.find(Role.class, id);

        RoleForm.updater(form, role);

        factory.from(RoleResource.class)
                .record(roleResource -> roleResource.delete(role.getId()))
                .build(form::addAction);

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
