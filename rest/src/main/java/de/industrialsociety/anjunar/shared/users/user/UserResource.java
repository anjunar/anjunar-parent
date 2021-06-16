package de.industrialsociety.anjunar.shared.users.user;

import de.industrialsociety.common.rest.api.AbstractRestEntity;
import de.industrialsociety.common.rest.api.Blob;
import de.industrialsociety.common.rest.api.meta.Input;

import java.time.LocalDate;
import java.util.Locale;

public class UserResource extends AbstractRestEntity {

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
}
