package de.bitvale.anjunar.home.timeline;

import de.bitvale.anjunar.shared.likeable.AbstractLikeableSearch;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;
import de.bitvale.common.rest.api.jaxrs.RestSort;
import de.bitvale.common.rest.api.jaxrs.provider.GenericSortProvider;

import java.util.List;

public class TimelineSearch extends AbstractLikeableSearch {

    @RestSort(GenericSortProvider.class)
    private List<String> sort;

    @RestPredicate(TextProvider.class)
    private String text;

    @RestPredicate(UserProvider.class)
    private UserSelect owner;

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public UserSelect getOwner() {
        return owner;
    }

    public void setOwner(UserSelect owner) {
        this.owner = owner;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
