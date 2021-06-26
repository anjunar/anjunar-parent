package de.bitvale.anjunar.control.roles;

import de.bitvale.anjunar.control.roles.role.RoleResource;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListMetaController;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Role;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("control/roles")
public class RolesControl implements ListMetaController<RoleResource, RolesSearch> {

    private final RolesService rolesService;

    private final Identity identity;

    @Inject
    public RolesControl(RolesService rolesService, Identity identity) {
        this.rolesService = rolesService;
        this.identity = identity;
    }

    public RolesControl() {
        this(null, null);
    }

    @Override
    @RolesAllowed({"Administrator", "User"})
    public MetaTable<RoleResource> list() {

        MetaTable<RoleResource> metaTable = new MetaTable<>(RoleResource.class, identity.getLanguage());

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("name", true, true),
                new Sortable("description", true, true)
        });

        identity.createLink("control/roles", "POST", "list", metaTable::addSource);

        return metaTable;
    }

    @Override
    @RolesAllowed({"Administrator", "User"})
    public Container<RoleResource> list(RolesSearch search) {

        List<Role> roles = rolesService.find(search);
        long count = rolesService.count(search);

        List<RoleResource> resources = new ArrayList<>();
        for (Role role : roles) {
            RoleResource resource = RoleResource.factory(role);

            resources.add(resource);

            identity.createLink("control/roles/role?id=" + role.getId(), "GET", "read", resource::addAction);
        }

        return new Container<>(resources, count);
    }
}


