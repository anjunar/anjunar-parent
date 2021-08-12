package de.bitvale.anjunar.pages.page.questions.question.answers.answer;

import de.bitvale.anjunar.control.users.UsersResource;
import de.bitvale.anjunar.control.users.UsersSearch;
import de.bitvale.anjunar.pages.page.Answer;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.anjunar.system.SystemNotificationService;
import de.bitvale.anjunar.timeline.SystemPost;
import de.bitvale.common.rest.MethodPredicate;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.FormResource;
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
public class AnswerResource implements FormResource<AnswerForm> {

    private final EntityManager entityManager;

    private final Identity identity;

    private final URLBuilderFactory factory;

    private final SystemNotificationService notificationService;

    @Inject
    public AnswerResource(EntityManager entityManager, Identity identity, URLBuilderFactory factory, SystemNotificationService notificationService) {
        this.entityManager = entityManager;
        this.identity = identity;
        this.factory = factory;
        this.notificationService = notificationService;
    }

    public AnswerResource() {
        this(null, null, null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed({"Administrator", "User"})
    public AnswerForm create(@QueryParam("topic") UUID uuid) {
        AnswerForm resource = new AnswerForm();

        resource.setTopic(uuid);
        resource.setOwner(new UserSelect());
        resource.setEditor(new Editor());
        resource.setViews(0);

        factory.from(AnswerResource.class)
                .record(answerResource -> answerResource.save(new AnswerForm()))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public AnswerForm read(UUID id) {

        Answer answer = entityManager.find(Answer.class, id);
        answer.setViews(answer.getViews() + 1);

        AnswerForm resource = AnswerForm.factory(answer);

        factory.from(AnswerResource.class)
                .record(answerResource -> answerResource.read(answer.getId()))
                .build(resource::addAction);

        factory.from(AnswerResource.class)
                .record(answerResource -> answerResource.update(answer.getId(), new AnswerForm()))
                .build(resource::addAction);

        factory.from(AnswerResource.class)
                .record(answerResource -> answerResource.delete(answer.getId()))
                .build(resource::addAction);

        Property property = resource.getMeta().find("owner");
        factory.from(UsersResource.class)
                .record(usersController -> usersController.list(new UsersSearch()))
                .build(property::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    public AnswerForm save(AnswerForm resource) {

        Answer answer = new Answer();

        AnswerForm.update(answer, resource, identity, entityManager);

        entityManager.persist(answer);

        resource.setId(answer.getId());

        notificationService.notifyOnAnswerSave(answer);

        factory.from(AnswerResource.class)
                .record(answerResource -> answerResource.read(answer.getId()))
                .build(resource::addAction);

        factory.from(AnswerResource.class)
                .record(answerResource -> answerResource.update(answer.getId(), new AnswerForm()))
                .build(resource::addAction);

        factory.from(AnswerResource.class)
                .record(answerResource -> answerResource.delete(answer.getId()))
                .build(resource::addAction);


        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    @MethodPredicate(AnswerOwnerPredicate.class)
    public AnswerForm update(UUID id, AnswerForm resource) {

        Answer answer = entityManager.find(Answer.class, id);

        AnswerForm.update(answer, resource, identity, entityManager);

        notificationService.notifyOnAnswerUpdate(answer);

        factory.from(AnswerResource.class)
                .record(answerResource -> answerResource.read(answer.getId()))
                .build(resource::addAction);

        factory.from(AnswerResource.class)
                .record(answerResource -> answerResource.delete(answer.getId()))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    @MethodPredicate(AnswerOwnerPredicate.class)
    public void delete(UUID id) {
        Answer answer = entityManager.getReference(Answer.class, id);
        entityManager.remove(answer);
    }
}
