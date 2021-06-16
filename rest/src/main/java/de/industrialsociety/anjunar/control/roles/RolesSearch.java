package de.industrialsociety.anjunar.control.roles;

import de.industrialsociety.anjunar.control.users.user.UserProvider;
import de.industrialsociety.common.rest.api.jaxrs.AbstractRestSearch;
import de.industrialsociety.common.rest.api.jaxrs.RestPredicate;
import de.industrialsociety.common.rest.api.jaxrs.RestSort;
import de.industrialsociety.common.rest.api.jaxrs.provider.GenericSortProvider;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

public class RolesSearch extends AbstractRestSearch {

    @RestSort(GenericSortProvider.class)
    @QueryParam("sort")
    private List<String> sort;

    @QueryParam("user")
    @RestPredicate(UserProvider.class)
    private UUID user;

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }
}
