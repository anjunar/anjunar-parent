package de.bitvale.anjunar.pages;

import de.bitvale.anjunar.shared.likeable.AbstractLikeableSearch;
import de.bitvale.anjunar.shared.system.Language;
import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;
import de.bitvale.common.rest.api.jaxrs.RestSort;
import de.bitvale.common.rest.api.jaxrs.provider.GenericSortProvider;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Locale;

public class PagesSearch extends AbstractLikeableSearch {

    @RestSort(GenericSortProvider.class)
    private List<String> sort;

    @RestPredicate(TitleProvider.class)
    private String title;

    @RestPredicate(WordProvider.class)
    private String text;

    @RestPredicate(LanguageProvider.class)
    private Language language;

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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
