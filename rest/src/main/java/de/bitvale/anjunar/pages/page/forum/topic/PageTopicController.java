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
        resource.setCreated(Instant.now());
        resource.setOwner(UserResource.factory(identity.getUser()));

        identity.createLink("pages/page/topics/topic", "POST", "save", resource::addAction);

        return resource;
    }

    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public PageTopicResource read(@QueryParam("id") UUID uuid) {
        Topic topic = entityManager.find(Topic.class, uuid);

        PageTopicResource resource = PageTopicResource.factory(topic);
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

        Topic topic = new Topic();

        PageTopicResource.updater(form, topic, identity, entityManager);

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

        PageTopicResource.updater(form, topic, identity, entityManager);

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
