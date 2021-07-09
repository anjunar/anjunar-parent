package de.bitvale.anjunar.pages.page.forum.topic;

import de.bitvale.anjunar.pages.page.forum.Question;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.rest.api.FormController;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page/topics/topic")
public class QuestionController implements FormController<QuestionResource> {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public QuestionController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public QuestionController() {
        this(null, null);
    }

    @Transactional
    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed({"Administrator", "User"})
    public QuestionResource create(@QueryParam("page") UUID page) {
        QuestionResource resource = new QuestionResource();

        resource.setPage(page);
        resource.setCreated(LocalDateTime.now());
        resource.setOwner(UserResource.factory(identity.getUser()));

        identity.createLink("pages/page/topics/topic", "POST", "save", resource::addAction);

        return resource;
    }

    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public QuestionResource read(@QueryParam("id") UUID uuid) {
        Question question = entityManager.find(Question.class, uuid);

        QuestionResource resource = QuestionResource.factory(question);
        if (identity.getUser().getId().equals(question.getOwner().getId())) {
            identity.createLink("pages/page/topics/topic?id=" + question.getId(), "GET", "read", resource::addAction);
            identity.createLink("pages/page/topics/topic?id=" + question.getId(), "PUT", "update", resource::addAction);
            identity.createLink("pages/page/topics/topic?id=" + question.getId(), "DELETE", "delete", resource::addAction);
        }

        identity.createLink("pages/page/topics/topic/replies?topic=" + question.getId(), "GET", "replies", resource::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public QuestionResource save(@Valid QuestionResource form) {

        UserResource owner = form.getOwner();
        if (! owner.getId().equals(identity.getUser().getId())) {
            throw new NotAuthorizedException("Not Allowed");
        }

        Question question = new Question();

        QuestionResource.updater(form, question, identity, entityManager);

        entityManager.persist(question);

        form.setId(question.getId());

        identity.createLink("pages/page/topics/topic?id=" + question.getId(), "GET", "read", form::addAction);
        identity.createLink("pages/page/topics/topic?id=" + question.getId(), "PUT", "update", form::addAction);
        identity.createLink("pages/page/topics/topic?id=" + question.getId(), "DELETE", "delete", form::addAction);
        identity.createLink("pages/page/topics/topic/replies", "GET", "replies", form::addLink);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public QuestionResource update(UUID id, @Valid QuestionResource form) {

        Question question = entityManager.find(Question.class, id);

        if (! question.getOwner().getId().equals(identity.getUser().getId())) {
            throw new NotAuthorizedException("Not Allowed");
        }

        QuestionResource.updater(form, question, identity, entityManager);

        identity.createLink("pages/page/topics/topic?id=" + question.getId(), "GET", "read", form::addAction);
        identity.createLink("pages/page/topics/topic?id=" + question.getId(), "PUT", "update", form::addAction);
        identity.createLink("pages/page/topics/topic?id=" + question.getId(), "DELETE", "delete", form::addAction);

        identity.createLink("pages/page/topics/topic/replies", "GET", "replies", form::addLink);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public void delete(UUID id) {
        Question question = entityManager.getReference(Question.class, id);

        User owner = question.getOwner();
        if (! owner.getId().equals(identity.getUser().getId())) {
            throw new NotAuthorizedException("Not Allowed");
        }

        entityManager.remove(question);
    }

}
