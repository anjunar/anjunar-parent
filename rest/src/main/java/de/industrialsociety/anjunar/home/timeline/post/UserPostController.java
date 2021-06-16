package de.industrialsociety.anjunar.home.timeline.post;

import de.industrialsociety.common.filedisk.Base64Resource;
import de.industrialsociety.common.filedisk.FileDiskUtils;
import de.industrialsociety.common.rest.Secured;
import de.industrialsociety.common.rest.api.Blob;
import de.industrialsociety.common.rest.api.FormController;
import de.industrialsociety.common.rest.api.meta.MetaForm;
import de.industrialsociety.common.rest.api.meta.Property;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.security.User;
import de.industrialsociety.common.security.UserImage;
import de.industrialsociety.anjunar.control.user.timeline.UserPost;
import de.industrialsociety.anjunar.shared.users.user.UserResource;
import de.industrialsociety.anjunar.timeline.TimelineImage;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.UUID;

@ApplicationScoped
@Path("home/timeline/post")
@Secured
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
    public MetaForm<PostResource> create() {
        PostResource resource = new PostResource();

        Blob image = new Blob();
        image.setData("");

        resource.setImage(image);
        UserResource userResource = new UserResource();
        resource.setOwner(userResource);

        User owner = identity.getUser();
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


        identity.createLink("home/timeline/post", "POST", "save", resource::addAction);

        return new MetaForm<>(resource, identity.getLanguage());
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public MetaForm<PostResource> read(UUID id) {

        UserPost post = entityManager.find(UserPost.class, id);

        PostResource resource1 = new PostResource();
        resource1.setId(post.getId());
        resource1.setText(post.getText());
        resource1.setCreated(post.getCreated());

        UserResource userResource = new UserResource();
        userResource.setId(post.getOwner().getId());
        userResource.setFirstName(post.getOwner().getFirstName());
        userResource.setLastName(post.getOwner().getLastName());
        userResource.setBirthDate(post.getOwner().getBirthDate());
        Blob image = new Blob();
        image.setName(post.getOwner().getPicture().getName());
        image.setLastModified(post.getOwner().getPicture().getLastModified());
        image.setData(FileDiskUtils.buildBase64(post.getOwner().getPicture().getType(), post.getOwner().getPicture().getSubType(), post.getOwner().getPicture().getData()));
        userResource.setImage(image);

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

        if (identity.getUser().equals(post.getOwner())) {
            identity.createLink("home/timeline/post?id=" + post.getId(), "PUT", "update", resource::addAction);
            identity.createLink("home/timeline/post?id=" + post.getId(), "DELETE", "delete", resource::addAction);
        }

        MetaForm<PostResource> metaForm = new MetaForm<>(resource, identity.getLanguage());

        Property likes = metaForm.find("likes");
        identity.createLink("control/users", "POST", "list", likes::addLink);

        identity.createLink("home/timeline/post/comments?post=" + post.getId(), "GET", "comments", metaForm::addLink);

        return metaForm;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public PostResource save(PostResource resource) {

        UserPost post = new UserPost();
        post.setUser(identity.getUser());

        post.setOwner(identity.getUser());
        post.setText(resource.getText());

        UserImage image = post.getOwner().getPicture();
        if (image != null) {
            String base64Resource = FileDiskUtils.buildBase64(image.getType(), image.getSubType(), image.getData());
            Blob blob = new Blob();
            blob.setName(image.getName());
            blob.setData(base64Resource);
            blob.setLastModified(image.getLastModified());

            UserResource userResource = new UserResource();
            userResource.setId(post.getOwner().getId());
            userResource.setFirstName(post.getOwner().getFirstName());
            userResource.setLastName(post.getOwner().getLastName());
            userResource.setImage(blob);
            resource.setOwner(userResource);
        }

        Blob resourceImage = resource.getImage();
        if (resourceImage != null && resourceImage.getData() != null && ! resourceImage.getData().equals("")) {
            Base64Resource base64Resource = FileDiskUtils.extractBase64(resourceImage.getData());
            TimelineImage timelineImage = new TimelineImage();
            timelineImage.setName(resourceImage.getName());
            timelineImage.setData(base64Resource.getData());
            timelineImage.setLastModified(resourceImage.getLastModified());
            timelineImage.setType(base64Resource.getType());
            timelineImage.setSubType(base64Resource.getSubType());
            post.setImage(timelineImage);
        }

        post.getLikes().clear();

        for (UserResource like : resource.getLikes()) {
            User user = identity.findUser(like.getId());
            post.getLikes().add(user);
        }

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
    public PostResource update(UUID id, PostResource resource) {

        UserPost post = entityManager.find(UserPost.class, id);

        post.setOwner(identity.getUser());
        post.setText(resource.getText());

        UserImage image = post.getOwner().getPicture();
        if (image != null) {
            String base64Resource = FileDiskUtils.buildBase64(image.getType(), image.getSubType(), image.getData());
            Blob blob = new Blob();
            blob.setName(image.getName());
            blob.setData(base64Resource);
            blob.setLastModified(image.getLastModified());

            UserResource userResource = new UserResource();
            userResource.setId(post.getOwner().getId());
            userResource.setFirstName(post.getOwner().getFirstName());
            userResource.setLastName(post.getOwner().getLastName());
            userResource.setImage(blob);
            resource.setOwner(userResource);
        }

        Blob resourceImage = resource.getImage();
        if (resourceImage != null && resourceImage.getData() != null && ! resourceImage.getData().equals("")) {
            Base64Resource base64Resource = FileDiskUtils.extractBase64(resourceImage.getData());
            TimelineImage timelineImage = new TimelineImage();
            timelineImage.setName(resourceImage.getName());
            timelineImage.setData(base64Resource.getData());
            timelineImage.setLastModified(resourceImage.getLastModified());
            timelineImage.setType(base64Resource.getType());
            timelineImage.setSubType(base64Resource.getSubType());
            post.setImage(timelineImage);
        }

        post.getLikes().clear();

        for (UserResource like : resource.getLikes()) {
            User user = identity.findUser(like.getId());
            post.getLikes().add(user);
        }

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
