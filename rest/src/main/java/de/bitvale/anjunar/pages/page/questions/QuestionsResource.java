package de.bitvale.anjunar.pages.page.questions;

import de.bitvale.anjunar.control.users.UsersResource;
import de.bitvale.anjunar.pages.page.Question;
import de.bitvale.anjunar.pages.page.questions.question.QuestionResource;
import de.bitvale.anjunar.pages.page.questions.question.QuestionForm;
import de.bitvale.anjunar.pages.page.questions.question.answers.AnswersResource;
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
@Path("pages/page/topics")
public class QuestionsResource implements ListResource<QuestionForm, QuestionsSearch> {

    private final QuestionsService service;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public QuestionsResource(QuestionsService service, Identity identity, URLBuilderFactory factory) {
        this.service = service;
        this.identity = identity;
        this.factory = factory;
    }

    public QuestionsResource() {
        this(null, null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable questions(@QueryParam("page") UUID page) {
        MetaTable metaTable = new MetaTable(QuestionForm.class);

        Property owner = metaTable.find("owner");
        factory.from(UsersResource.class)
                .record(usersControl -> usersControl.users())
                .build(owner::addLink);

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("owner", false, true),
                new Sortable("topic", true, true),
                new Sortable("text", false, false),
                new Sortable("views", false, false),
                new Sortable("created", true, false)
        });

        QuestionsSearch search = new QuestionsSearch();
        search.setPage(page);

        factory.from(QuestionsResource.class)
                .record(questionsResource -> questionsResource.list(search))
                .build(metaTable::addSource);

        return metaTable;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<QuestionForm> list(QuestionsSearch search) {

        long count = service.count(search);
        List<Question> questions = service.find(search);

        List<QuestionForm> resources = new ArrayList<>();

        for (Question question : questions) {
            QuestionForm resource = QuestionForm.factory(question);

            resources.add(resource);

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
        }

        Container<QuestionForm> container = new Container<>(resources, count);

        factory.from(QuestionResource.class)
                .record(questionResource -> questionResource.create(search.getPage()))
                .build(container::addLink);

        return container;
    }
}
