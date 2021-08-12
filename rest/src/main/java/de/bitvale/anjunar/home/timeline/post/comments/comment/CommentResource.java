package de.bitvale.anjunar.home.timeline.post.comments.comment;

import de.bitvale.anjunar.control.users.UsersResource;
import de.bitvale.anjunar.control.users.UsersSearch;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.common.rest.MethodPredicate;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.FormResource;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

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
public class CommentResource implements FormResource<CommentForm> {

    private final EntityManager entityManager;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public CommentResource(EntityManager entityManager, Identity identity, URLBuilderFactory factory) {
        this.entityManager = entityManager;
        this.identity = identity;
        this.factory = factory;
    }

    public CommentResource() {
        this(null, null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    public CommentForm create(@QueryParam("post") UUID post) {
        CommentForm resource = new CommentForm();

        User user = identity.getUser();

        resource.setPost(post);

        resource.setOwner(UserSelect.factory(user));

        factory.from(CommentResource.class)
                .record(commentResource -> commentResource.save(new CommentForm()))
                .build(resource::addAction);

        factory.from(UsersResource.class)
                .record(usersControl -> usersControl.list(new UsersSearch()))
                .build(resource::addSource);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public CommentForm read(UUID id) {

        Comment comment = entityManager.find(Comment.class, id);

        CommentForm resource = CommentForm.factory(comment);

        factory.from(CommentResource.class)
                .record(commentResource -> commentResource.update(comment.getId(), new CommentForm()))
                .build(resource::addAction);

        factory.from(CommentResource.class)
                .record(commentResource -> commentResource.delete(comment.getId()))
                .build(resource::addAction);

        factory.from(UsersResource.class)
                .record(usersControl -> usersControl.list(new UsersSearch()))
                .build(resource::addSource);

        Property likes = resource.getMeta().find("likes");
        factory.from(UsersResource.class)
                .rel("list")
                .record(usersControl -> usersControl.list(new UsersSearch()))
                .build(likes::addLink);

        Property owner = resource.getMeta().find("owner");
        factory.from(UsersResource.class)
                .rel("list")
                .record(usersControl -> usersControl.list(new UsersSearch()))
                .build(owner::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public CommentForm save(@Valid CommentForm resource) {

        Comment comment = new Comment();

        CommentForm.updater(resource, comment, identity, entityManager);

        entityManager.persist(comment);

        resource.setId(comment.getId());

        factory.from(CommentResource.class)
                .record(commentResource -> commentResource.read(comment.getId()))
                .build(resource::addAction);

        factory.from(CommentResource.class)
                .record(commentResource -> commentResource.update(comment.getId(), new CommentForm()))
                .build(resource::addAction);

        factory.from(CommentResource.class)
                .record(commentResource -> commentResource.delete(comment.getId()))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    @MethodPredicate(OwnerCommentIdentity.class)
    public CommentForm update(UUID id, @Valid CommentForm resource) {
        Comment comment = entityManager.find(Comment.class, id);

        CommentForm.updater(resource, comment, identity, entityManager);

        factory.from(CommentResource.class)
                .record(commentResource -> commentResource.read(comment.getId()))
                .build(resource::addAction);

        factory.from(CommentResource.class)
                .record(commentResource -> commentResource.update(comment.getId(), new CommentForm()))
                .build(resource::addAction);

        factory.from(CommentResource.class)
                .record(commentResource -> commentResource.delete(comment.getId()))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    @MethodPredicate(OwnerCommentIdentity.class)
    public void delete(UUID id) {
        Comment comment = entityManager.find(Comment.class, id);
        entityManager.remove(comment);
    }
}
