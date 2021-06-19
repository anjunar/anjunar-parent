package de.bitvale.anjunar.security.login;

import de.bitvale.common.rest.api.ActionsContainer;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.LinksContainer;
import de.bitvale.common.rest.api.meta.Input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class LoginResource implements LinksContainer, ActionsContainer {

    @Input(ignore = true)
    private final Set<Link> links = new HashSet<>();

    @Input(ignore = true)
    private final Set<Link> actions = new HashSet<>();

    @Input(placeholder = "de.bitvale.anjunar.security.login.LoginResource.firstName", type = "text")
    @Size(min = 3)
    private String firstName;

    @Input(placeholder = "de.bitvale.anjunar.security.login.LoginResource.lastName", type = "text")
    @Size(min = 3)
    private String lastName;

    @Input(placeholder = "de.bitvale.anjunar.security.login.LoginResource.birthdate", type = "date")
    @NotNull
    private LocalDate birthdate;

    @Input(placeholder = "de.bitvale.anjunar.security.login.LoginResource.password", type = "password")
    @Size(min = 3)
    private String password;

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

    @Override
    public Set<Link> getLinks() {
        return links;
    }

    @Override
    public boolean addLink(Link link) {
        return links.add(link);
    }
}
