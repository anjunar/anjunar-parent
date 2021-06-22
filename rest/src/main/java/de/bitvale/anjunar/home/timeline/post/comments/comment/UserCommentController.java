package de.bitvale.anjunar.home.timeline.post.comments.comment;

import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.anjunar.timeline.Post;
import de.bitvale.common.filedisk.FileDiskUtils;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.FormController;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import de.bitvale.common.security.UserImage;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.UUID;

@ApplicationScoped
@Path("/home/timeline/post/comments/comment")
public class UserCommentController implements FormController<CommentResource> {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public UserCommentController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public UserCommentController() {
        this(null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    public CommentResource create(@QueryParam("post") UUID post) {
        CommentResource resource = new CommentResource();

        User user = identity.getUser();

        resource.setPost(post);

        resource.setOwner(UserResource.factory(user));

        identity.createLink("home/timeline/post/comments/comment", "POST", "save", resource::addAction);
        identity.createLink("control/users", "POST", "likes", resource::addSource);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public CommentResource read(UUID id) {

        Comment comment = entityManager.find(Comment.class, id);

        CommentResource resource = CommentResource.factory(comment);

        if (identity.getUser().equals(comment.getOwner())) {
            identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "PUT", "update", resource::addAction);
            identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "DELETE", "delete", resource::addAction);
        }

        identity.createLink("control/users", "POST", "likes", resource::addSource);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public CommentResource save(@Valid CommentResource resource) {

        Comment comment = new Comment();

        CommentResource.updater(resource, comment, identity, entityManager);

        entityManager.persist(comment);

        resource.setId(comment.getId());

        if (identity.getUser().equals(comment.getOwner())) {
            identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "GET", "read", resource::addAction);
            identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "PUT", "update", resource::addAction);
            identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "DELETE", "delete", resource::addAction);
        } else {
            identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "GET", "read", resource::addAction);
        }

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public CommentResource update(UUID id, @Valid CommentResource resource) {
        Comment comment = entityManager.find(Comment.class, id);

        CommentResource.updater(resource, comment, identity, entityManager);

        if (identity.getUser().equals(comment.getOwner())) {
            identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "GET", "read", resource::addAction);
            identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "PUT", "update", resource::addAction);
            identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "DELETE", "delete", resource::addAction);
        } else {
            identity.createLink("home/timeline/post/comments/comment?id=" + comment.getId(), "GET", "read", resource::addAction);
        }

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public void delete(UUID id) {
        Comment comment = entityManager.find(Comment.class, id);
        entityManager.remove(comment);
    }
}
