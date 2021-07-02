package de.bitvale.anjunar.home.timeline.post.comments;

import de.bitvale.anjunar.home.timeline.post.comments.comment.CommentResource;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.common.filedisk.FileDiskUtils;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListController;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

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
@Path("home/timeline/post/comments")
public class UserCommentsController implements ListController<CommentResource, UserCommentsSearch> {

    private final UserCommentsService service;

    private final Identity identity;

    @Inject
    public UserCommentsController(UserCommentsService service, Identity identity) {
        this.service = service;
        this.identity = identity;
    }

    public UserCommentsController() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User"})
    public MetaTable<CommentResource> list(@QueryParam("post") UUID id) {
        MetaTable<CommentResource> metaTable = new MetaTable<>(CommentResource.class, identity.getLanguage());

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, true),
                new Sortable("text", false, true),
                new Sortable("post", false, true),
                new Sortable("owner", false, true),
                new Sortable("likes", false, true)
        });

        Property property = metaTable.find("owner");
        identity.createLink("control/users", "POST", "list", property::addLink);

        identity.createLink("home/timeline/post/comments?post=" + id, "POST", "list", metaTable::addSource);

        return metaTable;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public Container<CommentResource> list(UserCommentsSearch search) {

        Identity identity = service.identity();
        List<Comment> comments = service.find(search);
        long count = service.count(search);

        List<CommentResource> resources = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResource resource = CommentResource.factory(comment);

            resources.add(resource);

            if (identity.getUser().equals(comment.getOwner())) {
                identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "GET", "read", resource::addAction);
                identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "PUT", "update", resource::addAction);
                identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "DELETE", "delete", resource::addAction);
            } else {
                identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "GET", "read", resource::addAction);
            }

        }

        Container<CommentResource> container = new Container<>(resources, count);

        identity.createLink("home/timeline/post/comments/comment/create?post=" + search.getPost(), "GET", "create", container::addLink);

        return container;
    }

}
