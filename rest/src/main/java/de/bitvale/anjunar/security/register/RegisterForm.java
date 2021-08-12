package de.bitvale.anjunar.security.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.bitvale.common.rest.api.ActionsContainer;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.rest.api.meta.MetaForm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NaturalId
public class RegisterForm implements ActionsContainer {

    @Size(min = 3, max = 80)
    @Input(type = "text")
    private String firstName;

    @Size(min = 3, max = 80)
    @Input(type = "text")
    private String lastName;

    @NotNull
    @Input(type = "date")
    private LocalDate birthDate;

    @Size(min = 3, max = 80)
    @Input(type = "password")
    private String password;

    @Input(ignore = true)
    private final Set<Link> links = new HashSet<>();

    @Input(ignore = true)
    private final Set<Link> actions = new HashSet<>();

    @Input(ignore = true)
    private final Set<Link> sources = new HashSet<>();

    @Input(ignore = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final MetaForm meta = new MetaForm(RegisterForm.class);

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

    public Set<Link> getSources() {
        return sources;
    }

    public boolean addSource(Link link) {
        return sources.add(link);
    }

    public Set<Link> getLinks() {
        return links;
    }

    public boolean addLink(Link link) {
        return links.add(link);
    }

    public MetaForm getMeta() {
        return meta;
    }
}
