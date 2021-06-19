package de.bitvale.anjunar.security.register;

import de.bitvale.common.rest.api.ActionsContainer;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.meta.Input;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class RegisterResource implements ActionsContainer {

    @Input(placeholder = "First Name", type = "text")
    private String firstName;

    @Input(placeholder = "Last Name", type = "text")
    private String lastName;

    @Input(placeholder = "Birthdate", type = "date")
    private LocalDate birthdate;

    @Input(placeholder = "Password", type = "password")
    private String password;

    @Input(ignore = true)
    private final Set<Link> actions = new HashSet<>();

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

    @Override
    public Set<Link> getActions() {
        return actions;
    }

    @Override
    public boolean addAction(Link link) {
        return actions.add(link);
    }
}
