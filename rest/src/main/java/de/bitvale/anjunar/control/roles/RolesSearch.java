package de.bitvale.anjunar.control.roles;

import de.bitvale.anjunar.control.users.user.UserProvider;
import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;
import de.bitvale.common.rest.api.jaxrs.RestSort;
import de.bitvale.common.rest.api.jaxrs.provider.GenericSortProvider;
import de.bitvale.common.rest.api.meta.Input;

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
