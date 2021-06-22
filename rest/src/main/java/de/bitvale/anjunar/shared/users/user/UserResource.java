package de.bitvale.anjunar.shared.users.user;

import de.bitvale.anjunar.control.roles.role.RoleResource;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Role;
import de.bitvale.common.security.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class UserResource extends AbstractRestEntity<UserResource> {

    @Input(placeholder = "First Name", type = "text")
    private String firstName;

    @Input(placeholder = "First Name", type = "text")
    private String lastName;

    @Input(placeholder = "Birthdate", type = "date")
    private LocalDate birthDate;

    @Input(placeholder = "Image", type = "image")
    private Blob image;

    @Input(placeholder = "Language", type = "text")
    private Locale language;

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

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public static UserResource factory(User user) {
        UserResource resource = new UserResource();
        resource.setId(user.getId());
        resource.setFirstName(user.getFirstName());
        resource.setLastName(user.getLastName());
        resource.setBirthDate(user.getBirthDate());
        resource.setLanguage(user.getLanguage());
        resource.setImage(Blob.factory(user.getPicture()));
        return resource;
    }

}
