package de.bitvale.anjunar.home.timeline.post.comments;

import de.bitvale.anjunar.shared.likeable.AbstractLikeableSearch;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;
import de.bitvale.common.rest.api.jaxrs.RestSort;
import de.bitvale.common.rest.api.jaxrs.provider.GenericSortProvider;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommentsSearch extends AbstractLikeableSearch {

    @RestSort(GenericSortProvider.class)
    private List<String> sort;

    @RestPredicate(UserPostProvider.class)
    private UUID post;

    @RestPredicate(TextProvider.class)
    private String text;

    @RestPredicate(OwnerProvider.class)
    private UserSelect owner;

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public UUID getPost() {
        return post;
    }

    public void setPost(UUID post) {
        this.post = post;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserSelect getOwner() {
        return owner;
    }

    public void setOwner(UserSelect owner) {
        this.owner = owner;
    }

}
