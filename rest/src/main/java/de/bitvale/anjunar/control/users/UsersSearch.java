package de.bitvale.anjunar.control.users;

import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;
import de.bitvale.common.rest.api.jaxrs.RestSort;
import de.bitvale.common.rest.api.jaxrs.provider.GenericSortProvider;

import java.util.List;

public class UsersSearch extends AbstractRestSearch {

    @RestSort(GenericSortProvider.class)
    private List<String> sort;

    @RestPredicate(NamingProvider.class)
    private String naming;

    @RestPredicate(FirstNameProvider.class)
    private String firstName;

    @RestPredicate(LastNameProvider.class)
    private String lastName;

    @RestPredicate(BirthDataProvider.class)
    private BirthDateForm birthDate;

    @RestPredicate(EMailProvider.class)
    private String email;

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
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

    public BirthDateForm getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(BirthDateForm birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNaming() {
        return naming;
    }

    public void setNaming(String naming) {
        this.naming = naming;
    }
}
