package de.bitvale.anjunar.home.timeline.post;

import de.bitvale.anjunar.timeline.*;
import de.bitvale.anjunar.control.users.UsersResource;
import de.bitvale.anjunar.control.users.UsersSearch;
import de.bitvale.anjunar.home.timeline.post.comments.CommentsResource;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.MethodPredicate;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.FormResource;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.security.Identity;

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
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
@Path("home/timeline/post")
public class PostResource implements FormResource<AbstractPostForm> {

    private final EntityManager entityManager;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public PostResource(EntityManager entityManager, Identity identity, URLBuilderFactory factory) {
        this.entityManager = entityManager;
        this.identity = identity;
        this.factory = factory;
    }

    public PostResource() {
        this(null, null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed({"Administrator", "User"})
    public AbstractPostForm create(@QueryParam("type") String type) {

        AbstractPostForm resource;

        switch (type) {
            case "image" : {
                resource = new ImagePostForm();
            } break;
            case "link" : {
                resource = new LinkPostForm();
            } break;
            default: {
                resource = new TextPostForm();
            } break;
        }

        resource.setOwner(UserSelect.factory(identity.getUser()));
        resource.setCreated(Instant.now());

        factory.from(PostResource.class)
                .record(postResource -> postResource.save(new TextPostForm()))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public AbstractPostForm read(UUID id) {

        AbstractPost post = entityManager.find(AbstractPost.class, id);

        AbstractPostForm resource = post.accept(new AbstractPostVisitor<>() {
            @Override
            public AbstractPostForm visit(ImagePost post) {
                return ImagePostForm.factory(post);
            }

            @Override
            public AbstractPostForm visit(LinkPost post) {
                return LinkPostForm.factory(post);
            }

            @Override
            public AbstractPostForm visit(TextPost post) {
                return TextPostForm.factory(post);
            }

            @Override
            public AbstractPostForm visit(SystemPost post) {
                return SystemPostForm.factory(post);
            }
        });

        factory.from(PostResource.class)
                .record(postResource -> postResource.update(post.getId(), new TextPostForm()))
                .build(resource::addAction);

        factory.from(PostResource.class)
                .record(postResource -> postResource.delete(post.getId()))
                .build(resource::addAction);

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


        factory.from(CommentsResource.class)
                .record(commentsResource -> commentsResource.comments(post.getId()))
                .build(resource::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public AbstractPostForm save(@Valid AbstractPostForm resource) {

        AbstractPost post = resource.accept(new AbstractPostFormVisitor<>() {
            @Override
            public AbstractPost visit(LinkPostForm form) {
                return new LinkPost();
            }

            @Override
            public AbstractPost visit(ImagePostForm form) {
                return new ImagePost();
            }

            @Override
            public AbstractPost visit(TextPostForm form) {
                return new TextPost();
            }

            @Override
            public AbstractPost visit(SystemPostForm post) {
                return new SystemPost();
            }
        });

        post.accept(new AbstractPostVisitor<>() {
            @Override
            public AbstractPost visit(ImagePost post) {
                return ImagePostForm.updater((ImagePostForm) resource, post, identity, entityManager);
            }

            @Override
            public AbstractPost visit(LinkPost post) {
                return LinkPostForm.updater((LinkPostForm) resource, post, identity, entityManager);
            }

            @Override
            public AbstractPost visit(TextPost post) {
                return TextPostForm.updater((TextPostForm) resource, post, identity, entityManager);
            }

            @Override
            public AbstractPost visit(SystemPost post) {
                return SystemPostForm.updater((SystemPostForm) resource, post, identity, entityManager);
            }
        });

        entityManager.persist(post);

        resource.setId(post.getId());

        factory.from(PostResource.class)
                .record(postResource -> postResource.read(post.getId()))
                .build(resource::addAction);

        factory.from(PostResource.class)
                .record(postResource -> postResource.update(post.getId(), new TextPostForm()))
                .build(resource::addAction);

        factory.from(PostResource.class)
                .record(postResource -> postResource.delete(post.getId()))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    @MethodPredicate(OwnerPostIdentity.class)
    public AbstractPostForm update(UUID id, @Valid AbstractPostForm resource) {

        AbstractPost post = entityManager.find(AbstractPost.class, id);

        post.accept(new AbstractPostVisitor<>() {
            @Override
            public AbstractPost visit(ImagePost post) {
                return ImagePostForm.updater((ImagePostForm) resource, post, identity, entityManager);
            }

            @Override
            public AbstractPost visit(LinkPost post) {
                return LinkPostForm.updater((LinkPostForm) resource, post, identity, entityManager);
            }

            @Override
            public AbstractPost visit(TextPost post) {
                return TextPostForm.updater((TextPostForm) resource, post, identity, entityManager);
            }

            @Override
            public AbstractPost visit(SystemPost post) {
                return SystemPostForm.updater((SystemPostForm) resource, post, identity, entityManager);
            }
        });

        factory.from(PostResource.class)
                .record(postResource -> postResource.read(post.getId()))
                .build(resource::addAction);

        factory.from(PostResource.class)
                .record(postResource -> postResource.update(post.getId(), new TextPostForm()))
                .build(resource::addAction);

        factory.from(PostResource.class)
                .record(postResource -> postResource.delete(post.getId()))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    @MethodPredicate(OwnerPostIdentity.class)
    public void delete(UUID id) {
        AbstractPost userPost = entityManager.find(AbstractPost.class, id);
        entityManager.remove(userPost);
    }
}
