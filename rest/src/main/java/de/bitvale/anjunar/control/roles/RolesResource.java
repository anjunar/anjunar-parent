package de.bitvale.anjunar.control.roles;

import de.bitvale.anjunar.control.roles.role.RoleResource;
import de.bitvale.anjunar.control.roles.role.RoleForm;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListResource;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Role;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("control/roles")
public class RolesResource implements ListResource<RoleForm, RolesSearch> {

    private final RolesService rolesService;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public RolesResource(RolesService rolesService, Identity identity, URLBuilderFactory factory) {
        this.rolesService = rolesService;
        this.identity = identity;
        this.factory = factory;
    }

    public RolesResource() {
        this(null, null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable roles() {

        MetaTable metaTable = new MetaTable(RoleForm.class);

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("name", true, true),
                new Sortable("description", true, true),
                new Sortable("created", true, true),
                new Sortable("modified", true, true)
        });

        factory.from(RolesResource.class)
                .record(rolesResource -> rolesResource.list(new RolesSearch()))
                .build(metaTable::addSource);

        return metaTable;
    }

    @Override
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<RoleForm> list(RolesSearch search) {

        List<Role> roles = rolesService.find(search);
        long count = rolesService.count(search);

        List<RoleForm> resources = new ArrayList<>();
        for (Role role : roles) {
            RoleForm resource = RoleForm.factory(role);

            resources.add(resource);

            factory.from(RoleResource.class)
                    .record(roleResource -> roleResource.read(role.getId()))
                    .build(resource::addAction);
        }

        return new Container<>(resources, count);
    }
}


