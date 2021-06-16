package de.industrialsociety.anjunar.pages;

import de.industrialsociety.common.rest.api.jaxrs.AbstractRestSearch;
import de.industrialsociety.common.rest.api.jaxrs.RestPredicate;
import de.industrialsociety.common.rest.api.jaxrs.RestSort;
import de.industrialsociety.common.rest.api.jaxrs.provider.GenericSortProvider;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Locale;

public class PagesLikeSearch extends AbstractRestSearch {

    @RestSort(GenericSortProvider.class)
    @QueryParam("sort")
    private List<String> sort;

    @QueryParam("title")
    @RestPredicate(TitleProvider.class)
    private String title = "";

    @QueryParam("lang")
    @RestPredicate(LanguageProvider.class)
    private Locale language;

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

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }
}
