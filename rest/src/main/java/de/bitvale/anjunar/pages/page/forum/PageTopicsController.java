package de.bitvale.anjunar.pages.page.forum;

import de.bitvale.anjunar.pages.page.forum.topic.PageTopicResource;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.filedisk.FileDiskUtils;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.ListController;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import de.bitvale.common.security.UserImage;

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
public class PageTopicsController implements ListController<PageTopicResource, PageTopicsSearch> {

    private final PageTopicsService service;

    private final Identity identity;

    @Inject
    public PageTopicsController(PageTopicsService service, Identity identity) {
        this.service = service;
        this.identity = identity;
    }

    public PageTopicsController() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable<PageTopicResource> list(@QueryParam("page") UUID page) {
        MetaTable<PageTopicResource> metaTable = new MetaTable<>(PageTopicResource.class, identity.getLanguage());

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
    public Container<PageTopicResource> list(PageTopicsSearch search) {

        long count = service.count(search);
        List<Topic> topics = service.find(search);

        List<PageTopicResource> resources = new ArrayList<>();

        for (Topic topic : topics) {
            PageTopicResource resource = new PageTopicResource();

            resource.setId(topic.getId());
            Editor editor = new Editor();
            editor.setText(topic.getText());
            editor.setHtml(topic.getHtml());
            resource.setEditor(editor);
            resource.setTopic(topic.getTopic());
            resource.setCreated(topic.getCreated());
            resource.setPage(topic.getPage().getId());

            UserResource owner = new UserResource();
            User user = topic.getOwner();
            owner.setId(user.getId());
            owner.setFirstName(user.getFirstName());
            owner.setLastName(user.getLastName());
            owner.setBirthDate(user.getBirthDate());

            Blob image = new Blob();
            UserImage picture = user.getPicture();
            image.setName(picture.getName());
            image.setLastModified(picture.getLastModified());
            image.setData(FileDiskUtils.buildBase64(picture.getType(), picture.getSubType(), picture.getData()));
            owner.setImage(image);
            resource.setOwner(owner);

            identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "GET", "read", resource::addAction);
            identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "PUT", "update", resource::addAction);
            identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "DELETE", "delete", resource::addAction);

            identity.createLink("pages/page/topics/topic/replies", "GET", "replies", resource::addLink);

            resources.add(resource);
        }

        Container<PageTopicResource> container = new Container<>(resources, count);

        identity.createLink("pages/page/topics/topic/create?page=" + search.getPage(), "GET", "create", container::addLink);

        return container;
    }
}
