package de.bitvale.anjunar.home.timeline;

import de.bitvale.anjunar.control.users.user.UserForm;
import de.bitvale.anjunar.shared.system.CreatedForm;
import de.bitvale.anjunar.shared.system.CreatedProvider;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;
import de.bitvale.common.rest.api.jaxrs.RestSort;
import de.bitvale.common.rest.api.jaxrs.provider.GenericSortProvider;

import java.util.List;
import java.util.Set;

public class TimelineSearch extends AbstractRestSearch {

    @RestSort(GenericSortProvider.class)
    private List<String> sort;

    @RestPredicate(TextProvider.class)
    private String text;

    @RestPredicate(UserProvider.class)
    private UserSelect owner;

    @RestPredicate(CreatedProvider.class)
    private CreatedForm created;

    @RestPredicate(LikesProvider.class)
    private Set<UserForm> likes;

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

    public CreatedForm getCreated() {
        return created;
    }

    public void setCreated(CreatedForm created) {
        this.created = created;
    }

    public Set<UserForm> getLikes() {
        return likes;
    }

    public void setLikes(Set<UserForm> likes) {
        this.likes = likes;
    }
}
