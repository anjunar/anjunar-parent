package de.bitvale.anjunar.pages;

import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;
import de.bitvale.common.rest.api.jaxrs.RestSort;
import de.bitvale.common.rest.api.jaxrs.provider.GenericSortProvider;

import javax.ws.rs.QueryParam;
import java.util.List;

public class PagesSearch extends AbstractRestSearch {

    @QueryParam("sort")
    @RestSort(GenericSortProvider.class)
    private List<String> sort;

    @QueryParam("title")
    @RestPredicate(TitleProvider.class)
    private String title;

    @QueryParam("text")
    @RestPredicate(WordProvider.class)
    private String text;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
