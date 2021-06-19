package de.bitvale.anjunar.control.users.user;

import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.anjunar.control.roles.role.RoleResource;
import de.bitvale.common.rest.api.meta.Input;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NaturalId
public class UserResource extends AbstractRestEntity {

    @Size(min = 3, max = 80)
    @Input(placeholder = "de.bitvale.anjunar.control.users.user.UserResource.firstName.message", type = "text")
    private String firstName;

    @Size(min = 3, max = 80)
    @Input(placeholder = "de.bitvale.anjunar.control.users.user.UserResource.lastName.message", type = "text")
    private String lastName;

    @NotNull
    @Input(placeholder = "de.bitvale.anjunar.control.users.user.UserResource.birthdate.message", type = "date")
    private LocalDate birthdate;

    @Size(min = 3, max = 80)
    @Input(placeholder = "de.bitvale.anjunar.control.users.user.UserResource.password.message", type = "password")
    private String password;

    @Input(placeholder = "de.bitvale.anjunar.control.users.user.UserResource.picture.message", type = "image")
    private Blob picture;

    @Email
    @Input(placeholder = "de.bitvale.anjunar.control.users.user.UserResource.email.message", type = "email")
    private String email;

    @Input(placeholder = "de.bitvale.anjunar.control.users.user.UserResource.enabled.message", type = "checkbox")
    private boolean enabled;

    @Input(placeholder = "de.bitvale.anjunar.control.users.user.UserResource.roles.message", type = "lazymultiselect")
    private Set<RoleResource> roles = new HashSet<>();

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

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
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

    public Set<RoleResource> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleResource> roles) {
        this.roles = roles;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

}
