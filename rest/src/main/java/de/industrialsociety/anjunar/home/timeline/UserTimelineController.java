package de.industrialsociety.anjunar.home.timeline;

import de.industrialsociety.anjunar.home.timeline.post.PostResource;
import de.industrialsociety.common.filedisk.FileDiskUtils;
import de.industrialsociety.common.rest.Secured;
import de.industrialsociety.common.rest.api.Blob;
import de.industrialsociety.common.rest.api.Container;
import de.industrialsociety.common.rest.api.ListFormController;
import de.industrialsociety.common.rest.api.meta.MetaForm;
import de.industrialsociety.common.rest.api.meta.MetaTable;
import de.industrialsociety.common.rest.api.meta.Sortable;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.security.User;
import de.industrialsociety.common.security.UserImage;
import de.industrialsociety.anjunar.control.user.timeline.UserPost;
import de.industrialsociety.anjunar.shared.users.user.UserResource;
import de.industrialsociety.anjunar.timeline.TimelineImage;

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
@Secured
public class UserTimelineController implements ListFormController<PostResource, UserTimelineSearch> {

    private final UserTimelineService service;

    private final Identity identity;

    @Inject
    public UserTimelineController(UserTimelineService service, Identity identity) {
        this.service = service;
        this.identity = identity;
    }

    public UserTimelineController() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User"})
    public MetaTable<PostResource> list() {
        MetaTable<PostResource> metaTable = new MetaTable<>(PostResource.class, identity.getLanguage());

        metaTable.addSortable(new Sortable[] {
                new Sortable("id", false, false),
                new Sortable("text", true, true),
                new Sortable("image", false, true),
                new Sortable("owner", true, true),
                new Sortable("created", true, true),
                new Sortable("likes", false, true)
        });

        identity.createLink("home/timeline", "POST", "list", metaTable::addSource);

        return metaTable;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public Container<MetaForm<PostResource>> list(UserTimelineSearch search) {
        Identity identity = service.identity();

        List<UserPost> posts = service.find(search);
        long count = service.count(search);

        List<MetaForm<PostResource>> resources = new ArrayList<>();

        for (UserPost post : posts) {

            PostResource resource1 = new PostResource();
            resource1.setId(post.getId());
            resource1.setText(post.getText());
            resource1.setCreated(post.getCreated());

            UserResource userResource = new UserResource();
            User owner = post.getOwner();
            userResource.setId(owner.getId());
            userResource.setFirstName(owner.getFirstName());
            userResource.setLastName(owner.getLastName());
            userResource.setBirthDate(owner.getBirthDate());

            UserImage picture = owner.getPicture();
            if (picture != null) {
                Blob blob = new Blob();
                blob.setName(picture.getName());
                blob.setLastModified(picture.getLastModified());
                blob.setData(FileDiskUtils.buildBase64(picture.getType(), picture.getSubType(), picture.getData()));
                userResource.setImage(blob);
            }

            TimelineImage timelineImage = post.getImage();
            if (timelineImage != null) {
                Blob blob = new Blob();
                blob.setName(timelineImage.getName());
                blob.setLastModified(timelineImage.getLastModified());
                blob.setData(FileDiskUtils.buildBase64(timelineImage.getType(), timelineImage.getSubType(), timelineImage.getData()));
                resource1.setImage(blob);
            }

            resource1.setOwner(userResource);

            for (User like : post.getLikes()) {
                UserResource likeResource = new UserResource();
                likeResource.setId(like.getId());
                likeResource.setFirstName(like.getFirstName());
                likeResource.setLastName(like.getLastName());
                likeResource.setBirthDate(like.getBirthDate());
                resource1.getLikes().add(likeResource);
            }
            PostResource resource = resource1;

            resources.add(new MetaForm<>(resource, identity.getLanguage()));

            if (identity.getUser().equals(post.getOwner())) {
                identity.createLink("home/timeline/post?id=" + post.getId(), "GET", "read", resource::addAction);
                identity.createLink("home/timeline/post?id=" + post.getId(), "PUT", "update", resource::addAction);
                identity.createLink("home/timeline/post/comments", "GET", "comments", resource::addLink);
            } else {
                identity.createLink("home/timeline/post?id=" + post.getId(), "GET", "read", resource::addAction);
            }
        }

        Container<MetaForm<PostResource>> container = new Container<>(resources, count);

        identity.createLink("home/timeline/post/create", "GET", "create", container::addLink);

        return container;
    }

}
