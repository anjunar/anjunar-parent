package de.industrialsociety.anjunar.control.roles.role;

import de.industrialsociety.common.rest.api.AbstractRestEntity;
import de.industrialsociety.common.rest.api.meta.Input;

public class RoleResource extends AbstractRestEntity {

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
}
