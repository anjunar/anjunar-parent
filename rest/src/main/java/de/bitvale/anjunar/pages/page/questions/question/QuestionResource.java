package de.bitvale.anjunar.pages.page.questions.question;

import de.bitvale.anjunar.control.users.UsersResource;
import de.bitvale.anjunar.control.users.UsersSearch;
import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.pages.page.questions.question.answers.AnswersResource;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.anjunar.system.SystemNotificationService;
import de.bitvale.anjunar.timeline.SystemPost;
import de.bitvale.common.rest.MethodPredicate;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.FormResource;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.security.Identity;

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
public class QuestionResource implements FormResource<QuestionForm> {

    private final EntityManager entityManager;

    private final Identity identity;

    private final URLBuilderFactory factory;

    private final SystemNotificationService notificationService;

    @Inject
    public QuestionResource(EntityManager entityManager, Identity identity, URLBuilderFactory factory, SystemNotificationService notificationService) {
        this.entityManager = entityManager;
        this.identity = identity;
        this.factory = factory;
        this.notificationService = notificationService;
    }

    public QuestionResource() {
        this(null, null, null, null);
    }

    @Transactional
    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed({"Administrator", "User"})
    public QuestionForm create(@QueryParam("page") UUID page) {
        QuestionForm resource = new QuestionForm();

        resource.setPage(page);
        resource.setCreated(LocalDateTime.now());
        resource.setOwner(UserSelect.factory(identity.getUser()));

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.save(new QuestionForm()))
                .build(resource::addAction);

        return resource;
    }

    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public QuestionForm read(@QueryParam("id") UUID uuid) {
        Question question = entityManager.find(Question.class, uuid);
        question.setViews(question.getViews() + 1);

        QuestionForm resource = QuestionForm.factory(question);

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.read(question.getId()))
                .build(resource::addAction);

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.update(question.getId(), new QuestionForm()))
                .build(resource::addAction);

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.delete(question.getId()))
                .build(resource::addAction);

        factory.from(AnswersResource.class)
                .record(answersResource -> answersResource.answers(question.getId()))
                .build(resource::addLink);

        Property owner = resource.getMeta().find("owner");
        factory.from(UsersResource.class)
                .record(usersControl -> usersControl.list(new UsersSearch()))
                .build(owner::addLink);

        Property likes = resource.getMeta().find("likes");
        factory.from(UsersResource.class)
                .record(usersControl -> usersControl.list(new UsersSearch()))
                .build(likes::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public QuestionForm save(@Valid QuestionForm resource) {

        UserSelect owner = resource.getOwner();
        if (! owner.getId().equals(identity.getUser().getId())) {
            throw new NotAuthorizedException("Not Allowed");
        }

        Question question = new Question();

        QuestionForm.updater(resource, question, identity, entityManager);

        entityManager.persist(question);

        notificationService.notifyOnQuestionSave(question);

        resource.setId(question.getId());

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.read(question.getId()))
                .build(resource::addAction);

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.update(question.getId(), new QuestionForm()))
                .build(resource::addAction);

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.delete(question.getId()))
                .build(resource::addAction);

        factory.from(AnswersResource.class)
                .record(answersResource -> answersResource.answers(question.getId()))
                .build(resource::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    @MethodPredicate(QuestionOwnerPredicate.class)
    public QuestionForm update(UUID id, @Valid QuestionForm resource) {

        Question question = entityManager.find(Question.class, id);

        QuestionForm.updater(resource, question, identity, entityManager);

        notificationService.notifyOnQuestionUpdate(question);

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.read(question.getId()))
                .build(resource::addAction);

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.update(question.getId(), new QuestionForm()))
                .build(resource::addAction);

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.delete(question.getId()))
                .build(resource::addAction);

        factory.from(AnswersResource.class)
                .record(answersResource -> answersResource.answers(question.getId()))
                .build(resource::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    @MethodPredicate(QuestionOwnerPredicate.class)
    public void delete(UUID id) {
        Question question = entityManager.getReference(Question.class, id);

        entityManager.remove(question);
    }

}
