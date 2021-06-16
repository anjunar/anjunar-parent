package de.industrialsociety.anjunar.pages;

import de.industrialsociety.common.rest.api.jaxrs.AbstractRestSearch;
import de.industrialsociety.common.rest.api.jaxrs.RestSort;
import de.industrialsociety.common.rest.api.jaxrs.provider.GenericSortProvider;

import javax.ws.rs.QueryParam;
import java.util.List;

public class PagesWordSearch extends AbstractRestSearch {

    @RestSort(GenericSortProvider.class)
    @QueryParam("sort")
    private List<String> sort;

    @QueryParam("title")
    private String title = "";

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
