package de.bitvale.anjunar.pages.page.questions.question.answers;

import de.bitvale.anjunar.control.users.UsersResource;
import de.bitvale.anjunar.control.users.UsersSearch;
import de.bitvale.anjunar.pages.page.Answer;
import de.bitvale.anjunar.pages.page.questions.question.answers.answer.AnswerResource;
import de.bitvale.anjunar.pages.page.questions.question.answers.answer.AnswerForm;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListResource;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page/topics/topic/replies")
public class AnswersResource implements ListResource<AnswerForm, AnswersSearch> {

    private final AnswersService service;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public AnswersResource(AnswersService service, Identity identity, URLBuilderFactory factory) {
        this.service = service;
        this.identity = identity;
        this.factory = factory;
    }

    public AnswersResource() {
        this(null, null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable answers(@QueryParam("topic") UUID topic) {
        MetaTable metaTable = new MetaTable(AnswerForm.class);

        Property owner = metaTable.find("owner");
        factory.from(UsersResource.class)
                .record(usersController -> usersController.list(new UsersSearch()))
                .build(owner::addLink);

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("owner", false, true),
                new Sortable("editor", true, true)
        });


        AnswersSearch search = new AnswersSearch();
        search.setTopic(topic);
        factory.from(AnswersResource.class)
                .record(answersResource -> answersResource.list(search))
                .build(metaTable::addSource);

        return metaTable;
    }

    @Override
    @RolesAllowed({"Administrator", "User", "Guest"})
    @Transactional
    public Container<AnswerForm> list(AnswersSearch search) {

        long count = service.count(search);
        List<Answer> replies = service.find(search);

        List<AnswerForm> resources = new ArrayList<>();

        for (Answer answer : replies) {

            AnswerForm resource = AnswerForm.factory(answer);

            resources.add(resource);

            factory.from(AnswerResource.class)
                    .record(answerResource -> answerResource.read(answer.getId()))
                    .build(resource::addAction);

            factory.from(AnswerResource.class)
                    .record(answerResource -> answerResource.update(answer.getId(), new AnswerForm()))
                    .build(resource::addAction);

            factory.from(AnswerResource.class)
                    .record(answerResource -> answerResource.delete(answer.getId()))
                    .build(resource::addAction);
        }

        Container<AnswerForm> container = new Container<>(resources, count);

        factory.from(AnswerResource.class)
                .record(answerResource -> answerResource.create(search.getTopic()))
                .build(container::addLink);

        return container;
    }
}
