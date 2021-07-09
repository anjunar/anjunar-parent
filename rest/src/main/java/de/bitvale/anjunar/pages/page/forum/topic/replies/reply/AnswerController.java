package de.bitvale.anjunar.pages.page.forum.topic.replies.reply;

import de.bitvale.anjunar.pages.page.forum.Answer;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.FormController;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.security.Identity;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page/topics/topic/replies/reply")
public class AnswerController implements FormController<AnswerResource> {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public AnswerController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public AnswerController() {
        this(null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed({"Administrator", "User"})
    public AnswerResource create(@QueryParam("topic") UUID uuid) {
        AnswerResource resource = new AnswerResource();

        resource.setTopic(uuid);
        resource.setOwner(new UserResource());
        resource.setEditor(new Editor());

        identity.createLink("pages/page/topics/topic/replies/reply", "POST", "save", resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public AnswerResource read(UUID id) {

        Answer answer = entityManager.find(Answer.class, id);

        AnswerResource resource = AnswerResource.factory(answer);

        if (identity.getUser().getId().equals(answer.getOwner().getId())) {
            identity.createLink("pages/page/topics/topic/replies/reply?id=" + answer.getId(), "PUT", "update", resource::addAction);
            identity.createLink("pages/page/topics/topic/replies/reply?id=" + answer.getId(), "DELETE", "delete", resource::addAction);
        }

        Property property = resource.getMeta().find("owner");
        identity.createLink("control/users", "POST", "list", property::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public AnswerResource save(AnswerResource form) {

        Answer answer = new Answer();

        AnswerResource.update(answer, form, identity, entityManager);

        entityManager.persist(answer);

        form.setId(answer.getId());

        identity.createLink("pages/page/topics/topic/replies/reply?id=" + answer.getId(), "GET", "read", form::addAction);
        identity.createLink("pages/page/topics/topic/replies/reply?id=" + answer.getId(), "PUT", "update", form::addAction);
        identity.createLink("pages/page/topics/topic/replies/reply?id=" + answer.getId(), "DELETE", "delete", form::addAction);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public AnswerResource update(UUID id, AnswerResource form) {

        Answer answer = entityManager.find(Answer.class, id);

        if (!answer.getOwner().getId().equals(identity.getUser().getId())) {
            throw new NotAllowedException("Not Allowed");
        }

        AnswerResource.update(answer, form, identity, entityManager);

        identity.createLink("pages/page/topics/topic/replies/reply?id=" + answer.getId(), "GET", "read", form::addAction);
        identity.createLink("pages/page/topics/topic/replies/reply?id=" + answer.getId(), "DELETE", "delete", form::addAction);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public void delete(UUID id) {
        Answer answer = entityManager.getReference(Answer.class, id);
        entityManager.remove(answer);
    }
}
