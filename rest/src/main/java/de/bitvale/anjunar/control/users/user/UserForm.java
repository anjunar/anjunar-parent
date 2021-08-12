package de.bitvale.anjunar.control.users.user;

import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.anjunar.control.roles.role.RoleForm;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Image;
import de.bitvale.common.security.Role;
import de.bitvale.common.security.User;

import javax.persistence.EntityManager;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NaturalId
public class UserForm extends AbstractRestEntity {

    @Size(min = 3, max = 80)
    @Input(type = "text", naming = true)
    private String firstName;

    @Size(min = 3, max = 80)
    @Input(type = "text", naming = true)
    private String lastName;

    @NotNull
    @Input(type = "date")
    private LocalDate birthDate;

    @Size(min = 3, max = 80)
    @Input(type = "password")
    private String password;

    @Input(type = "image")
    private Blob picture = new Blob();

    @Email
    @Input(type = "email")
    private String email;

    @Input(type = "checkbox")
    private boolean enabled;

    @Input(type = "lazymultiselect")
    private Set<RoleForm> roles = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Blob getPicture() {
        return picture;
    }

    public void setPicture(Blob picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<RoleForm> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleForm> roles) {
        this.roles = roles;
    }

    public static UserForm factory(User user) {
        UserForm resource = new UserForm();
        resource.setId(user.getId());
        resource.setFirstName(user.getFirstName());
        resource.setLastName(user.getLastName());
        resource.setBirthDate(user.getBirthDate());
        resource.setPassword("s3cr3t");
        resource.setEmail(user.getEmail());
        resource.setEnabled(user.isEnabled());
        resource.setPicture(Blob.factory(user.getPicture()));
        Set<RoleForm> roleForms = new HashSet<>();
        for (Role role : user.getRoles()) {
            roleForms.add(RoleForm.factory(role));
        }
        resource.setRoles(roleForms);
        return resource;
    }

    public static User updater(UserForm resource, User user, EntityManager entityManager, Identity identity) {
        user.setFirstName(resource.getFirstName());
        user.setLastName(resource.getLastName());
        user.setBirthDate(resource.getBirthDate());
        user.setEnabled(resource.isEnabled());
        user.setEmail(resource.getEmail());
        if (user.getPicture() == null) {
            user.setPicture((Image) Blob.updater(resource.getPicture(), new Image()));
        } else {
            user.setPicture((Image) Blob.updater(resource.getPicture(), user.getPicture()));
        }

        Set<RoleForm> roleForms = resource.getRoles();
        if (identity.hasRole("Administrator")) {
            user.getRoles().clear();
            for (RoleForm roleForm : roleForms) {
                Role role = entityManager.find(Role.class, roleForm.getId());
                user.getRoles().add(role);
            }
        }

        return user;
    }

}
