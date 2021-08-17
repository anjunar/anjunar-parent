package de.bitvale.anjunar.shared.likeable;

import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.meta.Input;

import java.util.HashSet;
import java.util.Set;

public class AbstractLikeableRestEntity extends AbstractRestEntity {

    @Input(type = "number")
    private Integer views;

    @Input(type = "lazymultiselect")
    private final Set<UserSelect> likes = new HashSet<>();

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Set<UserSelect> getLikes() {
        return likes;
    }

}
