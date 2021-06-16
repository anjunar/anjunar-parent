package de.industrialsociety.anjunar.pages.page.images;

import de.industrialsociety.common.rest.api.jaxrs.AbstractRestSearch;
import de.industrialsociety.common.rest.api.jaxrs.RestPredicate;
import de.industrialsociety.common.rest.api.jaxrs.RestSort;
import de.industrialsociety.common.rest.api.jaxrs.provider.GenericSortProvider;
import de.industrialsociety.anjunar.pages.page.PageProvider;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

public class ImagesSearch extends AbstractRestSearch {

    @RestSort(GenericSortProvider.class)
    @QueryParam("sort")
    private List<String> sort;

    @RestPredicate(PageProvider.class)
    @QueryParam("page")
    private UUID page;

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public UUID getPage() {
        return page;
    }

    public void setPage(UUID page) {
        this.page = page;
    }

}
