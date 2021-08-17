package de.bitvale.anjunar.shared.likeable;

import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;

import java.util.Set;

public class AbstractLikeableSearch extends AbstractRestSearch {

    @RestPredicate(ViewsProvider.class)
    private Integer views;

    @RestPredicate(LikesProvider.class)
    private Set<UserSelect> likes;

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Set<UserSelect> getLikes() {
        return likes;
    }

    public void setLikes(Set<UserSelect> likes) {
        this.likes = likes;
    }
}
