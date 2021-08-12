package de.bitvale.anjunar.pages.page.questions;

import de.bitvale.common.rest.api.jaxrs.AbstractRestSearch;
import de.bitvale.common.rest.api.jaxrs.RestPredicate;
import de.bitvale.common.rest.api.jaxrs.RestSort;
import de.bitvale.common.rest.api.jaxrs.provider.GenericSortProvider;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

public class QuestionsSearch extends AbstractRestSearch {

    @QueryParam("sort")
    @RestSort(GenericSortProvider.class)
    private List<String> sort;

    @QueryParam("page")
    @RestPredicate(PageProvider.class)
    private UUID page;

    @QueryParam("topic")
    @RestPredicate(TopicProvider.class)
    private String topic;

    @QueryParam("text")
    @RestPredicate(TextProvider.class)
    private String text;

    @QueryParam("owner")
    @RestPredicate(OwnerProvider.class)
    private UUID owner;

    @QueryParam("views")
    @RestPredicate(ViewsProvider.class)
    private int views;

    @QueryParam("created")
    @RestPredicate(CreatedProvider.class)
    private String created;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
