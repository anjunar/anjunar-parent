package de.bitvale.anjunar.pages.page.forum.topic;

import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.filedisk.FileDiskUtils;
import de.bitvale.common.rest.api.Blob;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.FormController;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.pages.page.forum.Topic;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page/topics/topic")
public class PageTopicController implements FormController<PageTopicResource> {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public PageTopicController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public PageTopicController() {
        this(null, null);
    }

    @Transactional
    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed({"Administrator", "User"})
    public PageTopicResource create(@QueryParam("page") UUID page) {
        PageTopicResource resource = new PageTopicResource();
        resource.setPage(page);
        resource.setEditor(new Editor());
        resource.setCreated(Instant.now());
        UserResource owner = new UserResource();

        owner.setId(identity.getUser().getId());
        owner.setFirstName(identity.getUser().getFirstName());
        owner.setLastName(identity.getUser().getLastName());
        owner.setBirthDate(identity.getUser().getBirthDate());
        Blob image = new Blob();
        image.setName(identity.getUser().getPicture().getName());
        image.setLastModified(identity.getUser().getPicture().getLastModified());
        image.setData(FileDiskUtils.buildBase64(identity.getUser().getPicture().getType(), identity.getUser().getPicture().getSubType(), identity.getUser().getPicture().getData()));
        owner.setImage(image);

        resource.setOwner(owner);

        identity.createLink("pages/page/topics/topic", "POST", "save", resource::addAction);

        return resource;
    }

    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public PageTopicResource read(@QueryParam("id") UUID uuid) {
        Topic topic = entityManager.find(Topic.class, uuid);

        PageTopicResource resource = new PageTopicResource();
        resource.setId(topic.getId());
        resource.setTopic(topic.getTopic());
        Editor editor = new Editor();
        editor.setText(topic.getText());
        editor.setHtml(topic.getHtml());
        resource.setEditor(editor);
        resource.setViews(topic.getViews());
        resource.setCreated(topic.getCreated());

        resource.setPage(topic.getPage().getId());

        UserResource owner = new UserResource();
        User user = topic.getOwner();
        owner.setId(user.getId());
        owner.setFirstName(user.getFirstName());
        owner.setLastName(user.getLastName());
        owner.setBirthDate(user.getBirthDate());
        Blob blob = new Blob();
        blob.setName(user.getPicture().getName());
        blob.setLastModified(user.getPicture().getLastModified());
        blob.setData(FileDiskUtils.buildBase64(user.getPicture().getType(), user.getPicture().getSubType(), user.getPicture().getData()));
        owner.setImage(blob);
        resource.setOwner(owner);

        if (identity.getUser().getId().equals(topic.getOwner().getId())) {
            identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "GET", "read", resource::addAction);
            identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "PUT", "update", resource::addAction);
            identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "DELETE", "delete", resource::addAction);
        }

        identity.createLink("pages/page/topics/topic/replies?topic=" + topic.getId(), "GET", "replies", resource::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public PageTopicResource save(PageTopicResource form) {

        UserResource owner = form.getOwner();
        if (! owner.getId().equals(identity.getUser().getId())) {
            throw new NotAuthorizedException("Not Allowed");
        }

        Page page = entityManager.find(Page.class, form.getPage());

        Topic topic = new Topic();

        topic.setText(form.getEditor().getText());
        topic.setHtml(form.getEditor().getHtml());
        topic.setTopic(form.getTopic());
        topic.setOwner(identity.getUser());
        topic.setPage(page);

        entityManager.persist(topic);

        form.setId(topic.getId());

        identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "GET", "read", form::addAction);
        identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "PUT", "update", form::addAction);
        identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "DELETE", "delete", form::addAction);
        identity.createLink("pages/page/topics/topic/replies", "GET", "replies", form::addLink);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public PageTopicResource update(UUID id, PageTopicResource form) {

        Topic topic = entityManager.find(Topic.class, id);

        if (! topic.getOwner().getId().equals(identity.getUser().getId())) {
            throw new NotAuthorizedException("Not Allowed");
        }

        topic.setText(form.getEditor().getText());
        topic.setHtml(form.getEditor().getHtml());
        topic.setTopic(form.getTopic());
        topic.setOwner(identity.getUser());

        entityManager.persist(topic);

        identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "GET", "read", form::addAction);
        identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "PUT", "update", form::addAction);
        identity.createLink("pages/page/topics/topic?id=" + topic.getId(), "DELETE", "delete", form::addAction);

        identity.createLink("pages/page/topics/topic/replies", "GET", "replies", form::addLink);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public void delete(UUID id) {
        Topic topic = entityManager.getReference(Topic.class, id);

        User owner = topic.getOwner();
        if (! owner.getId().equals(identity.getUser().getId())) {
            throw new NotAuthorizedException("Not Allowed");
        }

        entityManager.remove(topic);
    }

}
