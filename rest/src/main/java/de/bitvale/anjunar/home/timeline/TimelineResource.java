package de.bitvale.anjunar.home.timeline;

import de.bitvale.anjunar.home.timeline.post.*;
import de.bitvale.anjunar.timeline.*;
import de.bitvale.anjunar.control.users.UsersResource;
import de.bitvale.anjunar.control.users.UsersSearch;
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
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("home/timeline")
public class TimelineResource implements ListResource<AbstractPostForm, TimelineSearch> {

    private final TimelineService service;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public TimelineResource(TimelineService service, Identity identity, URLBuilderFactory factory) {
        this.service = service;
        this.identity = identity;
        this.factory = factory;
    }

    public TimelineResource() {
        this(null, null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable timeline() {
        MetaTable metaTable = new MetaTable(AbstractPostForm.class);

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("text", true, true),
                new Sortable("owner", true, true),
                new Sortable("created", true, true),
                new Sortable("likes", false, false)
        });

        factory.from(TimelineResource.class)
                .record(timelineResource -> timelineResource.list(new TimelineSearch()))
                .build(metaTable::addSource);

        Property owner = metaTable.find("owner");
        factory.from(UsersResource.class)
                .record(usersControl -> usersControl.list(new UsersSearch()))
                .build(owner::addLink);

        Property likes = metaTable.find("likes");
        factory.from(UsersResource.class)
                .record(usersControl -> usersControl.list(new UsersSearch()))
                .build(likes::addLink);

        return metaTable;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<AbstractPostForm> list(TimelineSearch search) {
        List<AbstractPost> posts = service.find(search);
        long count = service.count(search);

        List<AbstractPostForm> resources = new ArrayList<>();

        for (AbstractPost post : posts) {

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

            resources.add(resource);

            factory.from(PostResource.class)
                    .record(postResource -> postResource.read(post.getId()))
                    .build(resource::addAction);

            factory.from(PostResource.class)
                    .record(postResource -> postResource.update(post.getId(), new TextPostForm()))
                    .build(resource::addAction);

        }

        Container<AbstractPostForm> container = new Container<>(resources, count);

        factory.from(PostResource.class)
                .record(postResource -> postResource.create("text"))
                .build(container::addLink);

        factory.from(PostResource.class)
                .record(postResource -> postResource.create("image"))
                .build(container::addLink);

        factory.from(PostResource.class)
                .record(postResource -> postResource.create("link"))
                .build(container::addLink);

        return container;
    }

}
