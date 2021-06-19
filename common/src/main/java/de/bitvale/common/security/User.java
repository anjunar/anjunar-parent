package de.bitvale.common.security;

import de.bitvale.common.ddd.AbstractAggregate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "ge_user")
public class User extends AbstractAggregate {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String question;

    private String password;

    private boolean enabled;

    private String email;

    private Locale language = Locale.ENGLISH;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserImage picture;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Relationship> relationships = new HashSet<>();

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

    public UserImage getPicture() {
        return picture;
    }

    public void setPicture(UserImage picture) {
        this.picture = picture;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }
}
