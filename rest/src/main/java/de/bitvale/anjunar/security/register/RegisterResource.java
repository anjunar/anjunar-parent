package de.bitvale.anjunar.security.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.bitvale.anjunar.security.login.LoginResource;
import de.bitvale.common.rest.api.ActionsContainer;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.rest.api.meta.MetaForm;
import de.bitvale.common.security.Identity;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
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

    @Input(ignore = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final MetaForm<LoginResource> meta;

    public RegisterResource() {
        Instance<Identity> instance = CDI.current().select(Identity.class);
        Identity identity = instance.stream().findAny().orElse(null);
        meta = new MetaForm<>(LoginResource.class, identity.getLanguage());
    }

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

    public MetaForm<LoginResource> getMeta() {
        return meta;
    }
}
