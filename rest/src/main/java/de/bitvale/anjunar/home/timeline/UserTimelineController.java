package de.bitvale.anjunar.home.timeline;

import de.bitvale.anjunar.control.user.timeline.UserPost;
import de.bitvale.anjunar.home.timeline.post.PostResource;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.anjunar.timeline.TimelineImage;
import de.bitvale.common.filedisk.FileDiskUtils;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListController;
import de.bitvale.common.rest.api.meta.MetaTable;
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
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("home/timeline")
public class UserTimelineController implements ListController<PostResource, UserTimelineSearch> {

    private final UserTimelineService service;

    private final Identity identity;

    @Inject
    public UserTimelineController(UserTimelineService service, Identity identity) {
        this.service = service;
        this.identity = identity;
    }

    public UserTimelineController() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User"})
    public MetaTable<PostResource> list() {
        MetaTable<PostResource> metaTable = new MetaTable<>(PostResource.class, identity.getLanguage());

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("text", true, true),
                new Sortable("image", false, true),
                new Sortable("owner", true, true),
                new Sortable("created", true, true),
                new Sortable("likes", false, true)
        });

        identity.createLink("home/timeline", "POST", "list", metaTable::addSource);

        return metaTable;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public Container<PostResource> list(UserTimelineSearch search) {
        Identity identity = service.identity();

        List<UserPost> posts = service.find(search);
        long count = service.count(search);

        List<PostResource> resources = new ArrayList<>();

        for (UserPost post : posts) {

            PostResource resource = PostResource.factory(post);

            resources.add(resource);

            if (identity.getUser().equals(post.getOwner())) {
                identity.createLink("home/timeline/post?id=" + post.getId(), "GET", "read", resource::addAction);
                identity.createLink("home/timeline/post?id=" + post.getId(), "PUT", "update", resource::addAction);
                identity.createLink("home/timeline/post/comments", "GET", "comments", resource::addLink);
            } else {
                identity.createLink("home/timeline/post?id=" + post.getId(), "GET", "read", resource::addAction);
            }
        }

        Container<PostResource> container = new Container<>(resources, count);

        identity.createLink("home/timeline/post/create", "GET", "create", container::addLink);

        return container;
    }

}
