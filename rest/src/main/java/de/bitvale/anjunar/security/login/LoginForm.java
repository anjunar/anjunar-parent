package de.bitvale.anjunar.security.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.bitvale.common.rest.api.ActionsContainer;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.LinksContainer;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.rest.api.meta.MetaForm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class LoginForm implements LinksContainer, ActionsContainer {

    @Input(ignore = true)
    private final Set<Link> links = new HashSet<>();

    @Input(ignore = true)
    private final Set<Link> actions = new HashSet<>();

    @Input(ignore = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final MetaForm meta = new MetaForm(LoginForm.class);

    @Input(type = "text")
    @Size(min = 3)
    private String firstname;

    @Input(type = "text")
    @Size(min = 3)
    private String lastname;

    @Input(type = "date")
    @NotNull
    private LocalDate birthday;

    @Input(type = "password")
    @Size(min = 3)
    private String password;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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

    public MetaForm getMeta() {
        return meta;
    }

}
