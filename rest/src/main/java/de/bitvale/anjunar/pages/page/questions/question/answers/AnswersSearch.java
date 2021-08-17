package de.bitvale.anjunar.pages.page.questions.question.answers;

import de.bitvale.anjunar.shared.likeable.AbstractLikeableSearch;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;
import de.bitvale.common.rest.api.jaxrs.RestSort;
import de.bitvale.common.rest.api.jaxrs.provider.GenericSortProvider;

import java.util.List;
import java.util.UUID;

public class AnswersSearch extends AbstractLikeableSearch {

    @RestSort(GenericSortProvider.class)
    private List<String> sort;

    @RestPredicate(TopicProvider.class)
    private UUID topic;

    @RestPredicate(EditorProvider.class)
    private String editor;

    @RestPredicate(UserProvider.class)
    private UserSelect owner;

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public UUID getTopic() {
        return topic;
    }

    public void setTopic(UUID topic) {
        this.topic = topic;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public UserSelect getOwner() {
        return owner;
    }

    public void setOwner(UserSelect owner) {
        this.owner = owner;
    }

}
