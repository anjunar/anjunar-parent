package de.industrialsociety.anjunar.home.timeline.post.comments;

import de.industrialsociety.anjunar.home.timeline.post.comments.comment.CommentResource;
import de.industrialsociety.common.filedisk.FileDiskUtils;
import de.industrialsociety.common.rest.Secured;
import de.industrialsociety.common.rest.api.Blob;
import de.industrialsociety.common.rest.api.Container;
import de.industrialsociety.common.rest.api.ListFormController;
import de.industrialsociety.common.rest.api.meta.MetaForm;
import de.industrialsociety.common.rest.api.meta.MetaTable;
import de.industrialsociety.common.rest.api.meta.Property;
import de.industrialsociety.common.rest.api.meta.Sortable;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.security.User;
import de.industrialsociety.anjunar.shared.users.user.UserResource;
import de.industrialsociety.anjunar.timeline.Comment;

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
@Secured
public class UserCommentsController implements ListFormController<CommentResource, UserCommentsSearch> {

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

        metaTable.addSortable(new Sortable[] {
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
    public Container<MetaForm<CommentResource>> list(UserCommentsSearch search) {

        Identity identity = service.identity();
        List<Comment> comments = service.find(search);
        long count = service.count(search);

        List<MetaForm<CommentResource>> resources = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResource resource = new CommentResource();

            resource.setId(comment.getId());
            resource.setText(comment.getText());

            resource.setPost(comment.getPost().getId());

            UserResource userResource = new UserResource();
            User owner = comment.getOwner();
            userResource.setId(owner.getId());
            userResource.setFirstName(owner.getFirstName());
            userResource.setLastName(owner.getLastName());
            userResource.setBirthDate(owner.getBirthDate());

            Blob picture = new Blob();
            if (owner.getPicture() != null) {
                picture.setData(FileDiskUtils.buildBase64(owner.getPicture().getType(), owner.getPicture().getSubType(), owner.getPicture().getData()));
                picture.setName(owner.getPicture().getName());
                picture.setLastModified(owner.getPicture().getLastModified());
            }
            userResource.setImage(picture);

            resource.setOwner(userResource);

            for (User like : comment.getLikes()) {
                UserResource likeResource = new UserResource();
                likeResource.setId(like.getId());
                likeResource.setFirstName(like.getFirstName());
                likeResource.setLastName(like.getLastName());
                likeResource.setBirthDate(like.getBirthDate());
                resource.getLikes().add(likeResource);
            }

            resources.add(new MetaForm<>(resource, identity.getLanguage()));

            if (identity.getUser().equals(comment.getOwner())) {
                identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "GET", "read", resource::addAction);
                identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "PUT", "update", resource::addAction);
                identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "DELETE", "delete", resource::addAction);
            } else {
                identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "GET", "read", resource::addAction);
            }

        }

        Container<MetaForm<CommentResource>> container = new Container<>(resources, count);

        identity.createLink("home/timeline/post/comments/comment/create?post=" + search.getPost(), "GET", "create", container::addLink);

        return container;
    }

}
