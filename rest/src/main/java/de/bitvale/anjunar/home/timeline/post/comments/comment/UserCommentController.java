package de.bitvale.anjunar.home.timeline.post.comments.comment;

import de.bitvale.common.filedisk.FileDiskUtils;
import de.bitvale.common.rest.Secured;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.FormController;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.anjunar.timeline.Post;
import de.bitvale.common.security.UserImage;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.UUID;

@ApplicationScoped
@Path("/home/timeline/post/comments/comment")
@Secured
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
        UserResource owner = new UserResource();
        owner.setId(user.getId());
        owner.setFirstName(user.getFirstName());
        owner.setLastName(user.getLastName());
        owner.setBirthDate(user.getBirthDate());
        Blob image = new Blob();
        UserImage picture = user.getPicture();
        image.setName(picture.getName());
        image.setData(FileDiskUtils.buildBase64(picture.getType(), picture.getSubType(), picture.getData()));
        image.setLastModified(picture.getLastModified());
        owner.setImage(image);

        resource.setOwner(owner);

        identity.createLink("home/timeline/post/comments/comment", "POST", "save", resource::addAction);
        identity.createLink("control/users", "POST", "likes", resource::addSource);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public CommentResource read(UUID id) {

        Comment comment = entityManager.find(Comment.class, id);

        CommentResource resource = new CommentResource();
        resource.setText(comment.getText());
        resource.setId(comment.getId());
        resource.setPost(comment.getPost().getId());
        UserResource owner = new UserResource();
        owner.setId(comment.getOwner().getId());
        owner.setFirstName(comment.getOwner().getFirstName());
        owner.setLastName(comment.getOwner().getLastName());
        owner.setBirthDate(comment.getOwner().getBirthDate());
        Blob image = new Blob();
        image.setName(comment.getOwner().getPicture().getName());
        image.setData(FileDiskUtils.buildBase64(comment.getOwner().getPicture().getType(), comment.getOwner().getPicture().getSubType(), comment.getOwner().getPicture().getData()));
        image.setLastModified(comment.getOwner().getPicture().getLastModified());
        owner.setImage(image);
        resource.setOwner(owner);

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
    public CommentResource save(CommentResource resource) {

        Post post = entityManager.find(Post.class, resource.getPost());
        User owner = identity.getUser();
        Comment comment = new Comment();

        comment.setText(resource.getText());
        comment.setPost(post);
        comment.setOwner(owner);

        UserResource userResource = new UserResource();
        userResource.setId(owner.getId());
        userResource.setFirstName(owner.getFirstName());
        userResource.setLastName(owner.getLastName());
        userResource.setBirthDate(owner.getBirthDate());

        resource.setOwner(userResource);

        comment.getLikes().clear();

        for (UserResource like : resource.getLikes()) {
            User user = identity.findUser(like.getId());
            comment.getLikes().add(user);
        }

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
    public CommentResource update(UUID id, CommentResource resource) {
        Post post = entityManager.find(Post.class, resource.getPost());
        User owner = identity.getUser();
        Comment comment = entityManager.find(Comment.class, id);

        comment.setText(resource.getText());
        comment.setPost(post);
        comment.setOwner(owner);

        UserResource userResource = new UserResource();
        userResource.setId(owner.getId());
        userResource.setFirstName(owner.getFirstName());
        userResource.setLastName(owner.getLastName());
        userResource.setBirthDate(owner.getBirthDate());

        resource.setOwner(userResource);

        comment.getLikes().clear();

        for (UserResource like : resource.getLikes()) {
            User user = identity.findUser(like.getId());
            comment.getLikes().add(user);
        }

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
