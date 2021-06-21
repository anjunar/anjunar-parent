package de.bitvale.anjunar.control.users;

import de.bitvale.anjunar.control.users.user.UserResource;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListMetaController;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@ApplicationScoped
@Path("control/users")
public class UsersControl implements ListMetaController<UserResource, UsersSearch> {

    private final UsersService service;

    private final Identity identity;

    @Inject
    public UsersControl(UsersService service, Identity identity) {
        this.service = service;
        this.identity = identity;
    }

    public UsersControl() {
        this(null, null);
    }

    @Override
    @RolesAllowed({"Administrator", "User"})
    public MetaTable<UserResource> list() {
        MetaTable<UserResource> metaTable = new MetaTable<>(UserResource.class, identity.getLanguage());

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("firstName", true, true),
                new Sortable("lastName", true, true),
                new Sortable("birthdate", true, true),
                new Sortable("email", true, true),
                new Sortable("enabled", false, true)
        });

        identity.createLink("control/users", "GET", "list", metaTable::addSource);

        return metaTable;
    }

    @RolesAllowed({"Administrator", "User"})
    public Container<UserResource> list(UsersSearch search) {

        List<User> users = service.find(search);
        long count = service.count(search);

        List<UserResource> resources = new ArrayList<>();
        for (User user : users) {
            UserResource resource = new UserResource();
            resource.setId(user.getId());
            resource.setFirstName(user.getFirstName());
            resource.setLastName(user.getLastName());
            resource.setBirthdate(user.getBirthDate());
            resource.setEnabled(user.isEnabled());
            if (user.getPicture() != null) {
                Base64.Encoder encoder = Base64.getMimeEncoder();
                byte[] encode = encoder.encode(user.getPicture().getData());
                String base64 = "data:" + user.getPicture().getType() + "/" + user.getPicture().getSubType() + ";base64,";

                Blob picture = new Blob();
                picture.setName(user.getPicture().getName());
                picture.setData(base64 + new String(encode));
                picture.setLastModified(user.getPicture().getLastModified());
                resource.setPicture(picture);
            }
            resources.add(resource);

            identity.createLink("control/users/user?id=" + user.getId(), "GET", "read", resource::addAction);

        }

        Container<UserResource> container = new Container<>(resources, count);

        identity.createLink("control/users/user/create", "GET", "create", container::addLink);

        return container;

    }

}
