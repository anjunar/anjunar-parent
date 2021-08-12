package de.bitvale.anjunar.shared.users.user;

import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.User;

import java.time.LocalDate;
import java.util.Locale;

public class UserSelect extends AbstractRestEntity {

    @Input(type = "text", naming = true)
    private String firstName;

    @Input(type = "text", naming = true)
    private String lastName;

    @Input(type = "date")
    private LocalDate birthDate;

    @Input(type = "image")
    private Blob image;

    @Input(type = "text")
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

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public static UserSelect factory(User user) {
        UserSelect resource = new UserSelect();
        resource.setId(user.getId());
        resource.setFirstName(user.getFirstName());
        resource.setLastName(user.getLastName());
        resource.setBirthDate(user.getBirthDate());
        resource.setLanguage(user.getLanguage());
        resource.setImage(Blob.factory(user.getPicture()));
        return resource;
    }

}
