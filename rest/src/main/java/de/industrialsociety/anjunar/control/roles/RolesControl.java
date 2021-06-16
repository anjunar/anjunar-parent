package de.industrialsociety.anjunar.control.roles;

import de.industrialsociety.common.rest.api.meta.MetaTable;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.rest.Secured;
import de.industrialsociety.common.rest.api.Container;
import de.industrialsociety.common.rest.api.ListMetaController;
import de.industrialsociety.common.rest.api.meta.Sortable;
import de.industrialsociety.common.security.Role;
import de.industrialsociety.anjunar.control.roles.role.RoleResource;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@Secured
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

        metaTable.addSortable(new Sortable[] {
                new Sortable("id", false, false),
                new Sortable("name", true, true),
                new Sortable("description", true, true)
        });

        identity.createLink("control/roles", "POST", "list", metaTable::addSource);

        return metaTable;
    }

    @Secured
    @Override
    @RolesAllowed({"Administrator", "User"})
    public Container<RoleResource> list(RolesSearch search) {

        List<Role> roles = rolesService.find(search);
        long count = rolesService.count(search);

        List<RoleResource> resources = new ArrayList<>();
        for (Role role : roles) {
            RoleResource resource = new RoleResource();
            resource.setId(role.getId());
            resource.setName(role.getName());
            resources.add(resource);

            identity.createLink("control/roles/role?id=" + role.getId(), "GET", "read", resource::addAction);

        }

        return new Container<>(resources, count);
    }
}


