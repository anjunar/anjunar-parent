package de.bitvale.common.security;

import de.bitvale.common.ddd.AbstractAggregate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "co_user")
public class User extends AbstractAggregate {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String question;

    private String password;

    private boolean enabled;

    private String email;

    private boolean emailConfirmed = false;

    private String emailConfirmationHash;

    private Locale language = Locale.ENGLISH;

    private String token;

    @ManyToOne(cascade = CascadeType.ALL)
    private Image picture;

    @ManyToMany
    private final Set<Role> roles = new HashSet<>();

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

    public void setBirthDate(LocalDate birthdate) {
        this.birthDate = birthdate;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getEmailConfirmationHash() {
        return emailConfirmationHash;
    }

    public void setEmailConfirmationHash(String emailConfirmationHash) {
        this.emailConfirmationHash = emailConfirmationHash;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
