package de.industrialsociety.anjunar.pages.page.forum.topic.replies;

import de.industrialsociety.anjunar.pages.page.forum.topic.replies.reply.TopicReplyResource;
import de.industrialsociety.anjunar.shared.users.user.UserResource;
import de.industrialsociety.common.filedisk.FileDiskUtils;
import de.industrialsociety.common.rest.Secured;
import de.industrialsociety.common.rest.api.Blob;
import de.industrialsociety.common.rest.api.Container;
import de.industrialsociety.common.rest.api.Editor;
import de.industrialsociety.common.rest.api.ListController;
import de.industrialsociety.common.rest.api.meta.MetaTable;
import de.industrialsociety.common.rest.api.meta.Property;
import de.industrialsociety.common.rest.api.meta.Sortable;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.security.User;
import de.industrialsociety.anjunar.pages.page.forum.Reply;

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
@Secured
public class TopicRelpiesController implements ListController<TopicReplyResource, TopicRepliesSearch> {

    private final TopicRepliesService service;

    private final Identity identity;

    @Inject
    public TopicRelpiesController(TopicRepliesService service, Identity identity) {
        this.service = service;
        this.identity = identity;
    }

    public TopicRelpiesController() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable<TopicReplyResource> list(@QueryParam("topic") UUID topic) {
        MetaTable<TopicReplyResource> metaTable = new MetaTable<>(TopicReplyResource.class, identity.getLanguage());

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
    public Container<TopicReplyResource> list(TopicRepliesSearch search) {

        long count = service.count(search);
        List<Reply> replies = service.find(search);

        List<TopicReplyResource> resources = new ArrayList<>();

        for (Reply reply : replies) {
            TopicReplyResource resource = new TopicReplyResource();
            resource.setId(reply.getId());
            Editor editor = new Editor();
            editor.setText(reply.getText());
            editor.setHtml(reply.getHtml());
            resource.setEditor(editor);
            resource.setTopic(reply.getTopic().getId());
            resource.setCreated(reply.getCreated());
            UserResource userResource = new UserResource();

            User owner = reply.getOwner();
            userResource.setId(owner.getId());
            userResource.setFirstName(owner.getFirstName());
            userResource.setLastName(owner.getLastName());
            userResource.setBirthDate(owner.getBirthDate());
            Blob image = new Blob();

            image.setName(owner.getPicture().getName());
            image.setLastModified(owner.getPicture().getLastModified());
            image.setData(FileDiskUtils.buildBase64(owner.getPicture().getType(), owner.getPicture().getSubType(), owner.getPicture().getData()));

            userResource.setImage(image);

            resource.setOwner(userResource);

            resources.add(resource);

            if (identity.getUser().getId().equals(reply.getOwner().getId())) {
                identity.createLink("pages/page/topics/topic/replies/reply?id=" + reply.getId(), "GET", "read", resource::addAction);
            }


        }

        Container<TopicReplyResource> container = new Container<>(resources, count);

        identity.createLink("pages/page/topics/topic/replies/reply/create?topic=" + search.getTopic(), "GET", "create", container::addLink);

        return container;
    }
}
