package de.bitvale.anjunar.control.roles.role;

import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Role;

import java.util.UUID;

public class RoleResource extends AbstractRestEntity<RoleResource> {

    @Input(placeholder = "Name", type = "text")
    private String name;

    @Input(placeholder = "Description", type = "text")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static RoleResource factory(Role role) {
        RoleResource resource = new RoleResource();
        resource.setId(role.getId());
        resource.setName(role.getName());
        resource.setDescription(role.getDescription());
        return resource;
    }

    public static Role updater(RoleResource resource, Role role) {
        role.setName(resource.getName());
        role.setDescription(resource.getDescription());
        return role;
    }
}
