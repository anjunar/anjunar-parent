package de.bitvale.anjunar.control.roles.role;

import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Role;

public class RoleForm extends AbstractRestEntity {

    @Input(type = "text", naming = true)
    private String name;

    @Input(type = "text")
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

    public static RoleForm factory(Role role) {
        RoleForm resource = new RoleForm();
        resource.setId(role.getId());
        resource.setName(role.getName());
        resource.setDescription(role.getDescription());
        return resource;
    }

    public static Role updater(RoleForm resource, Role role) {
        role.setName(resource.getName());
        role.setDescription(resource.getDescription());
        return role;
    }
}
