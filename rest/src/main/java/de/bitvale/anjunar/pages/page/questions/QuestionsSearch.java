package de.bitvale.anjunar.pages.page.questions;

import de.bitvale.anjunar.shared.likeable.AbstractLikeableSearch;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;
import de.bitvale.common.rest.api.jaxrs.RestSort;
import de.bitvale.common.rest.api.jaxrs.provider.GenericSortProvider;

import java.util.List;
import java.util.UUID;

public class QuestionsSearch extends AbstractLikeableSearch {

    @RestSort(GenericSortProvider.class)
    private List<String> sort;

    @RestPredicate(PageProvider.class)
    private UUID page;

    @RestPredicate(TopicProvider.class)
    private String topic;

    @RestPredicate(TextProvider.class)
    private String editor;

    @RestPredicate(OwnerProvider.class)
    private UserSelect owner;

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public UUID getPage() {
        return page;
    }

    public void setPage(UUID page) {
        this.page = page;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
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
