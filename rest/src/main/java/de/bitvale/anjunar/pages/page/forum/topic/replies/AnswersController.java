package de.bitvale.anjunar.pages.page.forum.topic.replies;

import de.bitvale.anjunar.pages.page.forum.Answer;
import de.bitvale.anjunar.pages.page.forum.topic.replies.reply.AnswerResource;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListController;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page/topics/topic/replies")
public class AnswersController implements ListController<AnswerResource, AnswersSearch> {

    private final AnswersService service;

    private final Identity identity;

    @Inject
    public AnswersController(AnswersService service, Identity identity) {
        this.service = service;
        this.identity = identity;
    }

    public AnswersController() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable<AnswerResource> list(@QueryParam("topic") UUID topic) {
        MetaTable<AnswerResource> metaTable = new MetaTable<>(AnswerResource.class, identity.getLanguage());

        Property owner = metaTable.find("owner");
        identity.createLink("control/users", "POST", "list", owner::addLink);

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("owner", false, true),
                new Sortable("editor", true, true)
        });

        identity.createLink("pages/page/topics/topic/replies?topic=" + topic, "POST", "list", metaTable::addSource);

        return metaTable;
    }

    @Override
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<AnswerResource> list(AnswersSearch search) {

        long count = service.count(search);
        List<Answer> replies = service.find(search);

        List<AnswerResource> resources = new ArrayList<>();

        for (Answer answer : replies) {

            AnswerResource resource = AnswerResource.factory(answer);

            resources.add(resource);

            if (identity.getUser().getId().equals(answer.getOwner().getId())) {
                identity.createLink("pages/page/topics/topic/replies/reply?id=" + answer.getId(), "GET", "read", resource::addAction);
            }
        }

        Container<AnswerResource> container = new Container<>(resources, count);

        identity.createLink("pages/page/topics/topic/replies/reply/create?topic=" + search.getTopic(), "GET", "create", container::addLink);

        return container;
    }
}
