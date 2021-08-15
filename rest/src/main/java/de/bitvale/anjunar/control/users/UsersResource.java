package de.bitvale.anjunar.control.users;

import de.bitvale.anjunar.control.users.user.UserResource;
import de.bitvale.anjunar.control.users.user.UserForm;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListResource;
import de.bitvale.common.rest.api.meta.MetaTable;
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
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("control/users")
public class UsersResource implements ListResource<UserForm, UsersSearch> {

    private final UsersService service;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public UsersResource(UsersService service, Identity identity, URLBuilderFactory factory) {
        this.service = service;
        this.identity = identity;
        this.factory = factory;
    }

    public UsersResource() {
        this(null, null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable users() {
        MetaTable metaTable = new MetaTable(UserForm.class);

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("firstName", true, true),
                new Sortable("lastName", true, true),
                new Sortable("birthDate", true, true),
                new Sortable("email", true, true),
                new Sortable("enabled", false, true)
        });

        factory.from(UsersResource.class)
                .record(usersControl -> usersControl.list(new UsersSearch()))
                .build(metaTable::addSource);

        return metaTable;
    }

    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<UserForm> list(UsersSearch search) {

        List<User> users = service.find(search);
        long count = service.count(search);

        List<UserForm> resources = new ArrayList<>();
        for (User user : users) {
            UserForm resource = UserForm.factory(user);

            resources.add(resource);

            factory.from(UserResource.class)
                    .record(userResource -> userResource.read(user.getId()))
                    .build(resource::addAction);
        }

        Container<UserForm> container = new Container<>(resources, count);

        factory.from(UserResource.class)
                .record(UserResource::create)
                .build(container::addLink);

        return container;

    }

}
