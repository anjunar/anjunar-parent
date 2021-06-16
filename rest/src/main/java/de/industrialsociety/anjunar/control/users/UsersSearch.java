package de.industrialsociety.anjunar.control.users;

import de.industrialsociety.common.rest.api.jaxrs.AbstractRestSearch;
import de.industrialsociety.common.rest.api.jaxrs.RestPredicate;
import de.industrialsociety.common.rest.api.jaxrs.RestSort;
import de.industrialsociety.common.rest.api.jaxrs.provider.GenericLikeNameProvider;
import de.industrialsociety.common.rest.api.jaxrs.provider.GenericSortProvider;

import javax.ws.rs.QueryParam;
import java.util.List;

public class UsersSearch extends AbstractRestSearch {

    @RestSort(GenericSortProvider.class)
    @QueryParam("sort")
    private List<String> sort;

    @QueryParam("firstName")
    @RestPredicate(GenericLikeNameProvider.class)
    private String firstName;

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
}
