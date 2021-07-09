package de.bitvale.anjunar.pages.page.forum;

import de.bitvale.anjunar.pages.page.forum.topic.QuestionResource;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListController;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page/topics")
public class QuestionsController implements ListController<QuestionResource, QuestionsSearch> {

    private final QuestionsService service;

    private final Identity identity;

    @Inject
    public QuestionsController(QuestionsService service, Identity identity) {
        this.service = service;
        this.identity = identity;
    }

    public QuestionsController() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable<QuestionResource> list(@QueryParam("page") UUID page) {
        MetaTable<QuestionResource> metaTable = new MetaTable<>(QuestionResource.class, identity.getLanguage());

        Property owner = metaTable.find("owner");
        identity.createLink("control/users", "POST", "list", owner::addLink);

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("owner", false, true),
                new Sortable("topic", true, true),
                new Sortable("text", false, false),
                new Sortable("views", false, false),
                new Sortable("created", true, false)
        });

        identity.createLink("pages/page/topics?page=" + page, "POST", "list", metaTable::addSource);

        return metaTable;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<QuestionResource> list(QuestionsSearch search) {

        long count = service.count(search);
        List<Question> questions = service.find(search);

        List<QuestionResource> resources = new ArrayList<>();

        for (Question question : questions) {
            QuestionResource resource = QuestionResource.factory(question);

            resources.add(resource);

            identity.createLink("pages/page/topics/topic?id=" + question.getId(), "GET", "read", resource::addAction);
            identity.createLink("pages/page/topics/topic?id=" + question.getId(), "PUT", "update", resource::addAction);
            identity.createLink("pages/page/topics/topic?id=" + question.getId(), "DELETE", "delete", resource::addAction);

            identity.createLink("pages/page/topics/topic/replies", "GET", "replies", resource::addLink);
        }

        Container<QuestionResource> container = new Container<>(resources, count);

        identity.createLink("pages/page/topics/topic/create?page=" + search.getPage(), "GET", "create", container::addLink);

        return container;
    }
}
