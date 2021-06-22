package de.bitvale.anjunar.home.timeline.post;

import de.bitvale.anjunar.control.user.timeline.UserPost;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.anjunar.timeline.TimelineImage;
import de.bitvale.common.filedisk.Base64Resource;
import de.bitvale.common.filedisk.FileDiskUtils;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.FormController;
import de.bitvale.common.rest.api.meta.Property;
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
import java.util.UUID;

@ApplicationScoped
@Path("home/timeline/post")
public class UserPostController implements FormController<PostResource> {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public UserPostController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public UserPostController() {
        this(null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed({"Administrator", "User"})
    public PostResource create() {
        PostResource resource = new PostResource();

        resource.setOwner(UserResource.factory(identity.getUser()));

        identity.createLink("home/timeline/post", "POST", "save", resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public PostResource read(UUID id) {

        UserPost post = entityManager.find(UserPost.class, id);

        PostResource resource = PostResource.factory(post);

        if (identity.getUser().equals(post.getOwner())) {
            identity.createLink("home/timeline/post?id=" + post.getId(), "PUT", "update", resource::addAction);
            identity.createLink("home/timeline/post?id=" + post.getId(), "DELETE", "delete", resource::addAction);
        }

        Property likes = resource.getMeta().find("likes");
        identity.createLink("control/users", "POST", "list", likes::addLink);

        identity.createLink("home/timeline/post/comments?post=" + post.getId(), "GET", "comments", resource::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public PostResource save(@Valid PostResource resource) {

        UserPost post = new UserPost();

        PostResource.updater(resource, post, identity, entityManager);

        entityManager.persist(post);

        resource.setId(post.getId());


        if (identity.getUser().equals(post.getOwner())) {
            identity.createLink("home/timeline/post?id=" + post.getId(), "GET", "read", resource::addAction);
            identity.createLink("home/timeline/post?id=" + post.getId(), "PUT", "update", resource::addAction);
            identity.createLink("home/timeline/post?id=" + post.getId(), "DELETE", "delete", resource::addAction);
        }

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public PostResource update(UUID id, @Valid PostResource resource) {

        UserPost post = entityManager.find(UserPost.class, id);

        PostResource.updater(resource, post, identity, entityManager);

        if (identity.getUser().equals(post.getOwner())) {
            identity.createLink("home/timeline/post?id=" + post.getId(), "GET", "read", resource::addAction);
            identity.createLink("home/timeline/post?id=" + post.getId(), "PUT", "update", resource::addAction);
            identity.createLink("home/timeline/post?id=" + post.getId(), "DELETE", "delete", resource::addAction);
        }

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public void delete(UUID id) {
        UserPost userPost = entityManager.find(UserPost.class, id);
        entityManager.remove(userPost);
    }
}
