package de.bitvale.anjunar.home.timeline.post.comments;

import de.bitvale.anjunar.control.users.UsersResource;
import de.bitvale.anjunar.control.users.UsersSearch;
import de.bitvale.anjunar.home.timeline.post.comments.comment.CommentForm;
import de.bitvale.anjunar.home.timeline.post.comments.comment.CommentResource;
import de.bitvale.anjunar.timeline.Comment;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListResource;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;

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
public class CommentsResource implements ListResource<CommentForm, CommentsSearch> {

    private final CommentsService service;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public CommentsResource(CommentsService service, Identity identity, URLBuilderFactory factory) {
        this.service = service;
        this.identity = identity;
        this.factory = factory;
    }

    public CommentsResource() {
        this(null, null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable comments(@QueryParam("post") UUID id) {
        MetaTable metaTable = new MetaTable(CommentForm.class);

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("text", true, true),
                new Sortable("post", false, false),
                new Sortable("owner", false, true),
                new Sortable("created", true, true),
                new Sortable("modified", true, true),
                new Sortable("likes", false, false)
        });

        Property owner = metaTable.find("owner");
        factory.from(UsersResource.class)
                .record(usersResource -> usersResource.list(new UsersSearch()))
                .build(owner::addLink);

        Property likes = metaTable.find("likes");
        factory.from(UsersResource.class)
                .record(usersResource -> usersResource.list(new UsersSearch()))
                .build(likes::addLink);

        CommentsSearch search = new CommentsSearch();
        search.setPost(id);
        factory.from(CommentsResource.class)
                .record(commentsResource -> commentsResource.list(search))
                .build(metaTable::addSource);

        return metaTable;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<CommentForm> list(CommentsSearch search) {

        Identity identity = service.identity();
        List<Comment> comments = service.find(search);
        long count = service.count(search);

        List<CommentForm> resources = new ArrayList<>();

        for (Comment comment : comments) {
            CommentForm resource = CommentForm.factory(comment);

            resources.add(resource);

            factory.from(CommentResource.class)
                    .record(commentResource -> commentResource.read(comment.getId()))
                    .build(resource::addAction);

            factory.from(CommentResource.class)
                    .record(commentResource -> commentResource.update(comment.getId(), new CommentForm()))
                    .build(resource::addAction);

            factory.from(CommentResource.class)
                    .record(commentResource -> commentResource.delete(comment.getId()))
                    .build(resource::addAction);

        }

        Container<CommentForm> container = new Container<>(resources, count);

        factory.from(CommentResource.class)
                .record(commentResource -> commentResource.create(search.getPost()))
                .build(container::addLink);

        return container;
    }

}
