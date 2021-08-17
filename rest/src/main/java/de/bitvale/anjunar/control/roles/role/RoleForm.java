package de.bitvale.anjunar.control.roles.role;

import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.AbstractRestEntityConverter;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Role;

import javax.persistence.EntityManager;

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

    public static class RoleFormConverter extends AbstractRestEntityConverter<Role, RoleForm> {

        public static RoleFormConverter INSTANCE = new RoleFormConverter();

        @Override
        public RoleForm factory(RoleForm resource, Role role) {
            resource.setId(role.getId());
            resource.setName(role.getName());
            resource.setDescription(role.getDescription());
            return super.factory(resource, role);
        }

        @Override
        public Role updater(RoleForm resource, Role role, EntityManager entityManager, Identity identity) {
            role.setName(resource.getName());
            role.setDescription(resource.getDescription());
            return role;
        }
    }

    public static RoleForm factory(Role role) {
        return RoleFormConverter.INSTANCE.factory(new RoleForm(), role);
    }

    public static Role updater(RoleForm resource, Role role, EntityManager entityManager, Identity identity) {
        return RoleFormConverter.INSTANCE.updater(resource, role, entityManager, identity);
    }


}
