package de.bitvale.anjunar.pages.page;

import de.bitvale.anjunar.ApplicationResource;
import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.pages.PagesResource;
import de.bitvale.anjunar.pages.PagesSearch;
import de.bitvale.anjunar.pages.page.questions.QuestionsResource;
import de.bitvale.anjunar.system.SystemNotificationService;
import de.bitvale.anjunar.timeline.SystemPost;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.security.Identity;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page")
public class PageResource {

    private final EntityManager entityManager;

    private final Identity identity;

    private final URLBuilderFactory factory;

    private final SystemNotificationService notificationService;

    @Inject
    public PageResource(EntityManager entityManager, Identity identity, URLBuilderFactory factory, SystemNotificationService notificationService) {
        this.entityManager = entityManager;
        this.identity = identity;
        this.factory = factory;
        this.notificationService = notificationService;
    }

    public PageResource() {
        this(null, null, null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed({"Administrator", "User"})
    public PageForm create() {

        PageForm pageForm = new PageForm();

        factory.from(PageResource.class)
                .record(pageResource -> pageResource.save(new PageForm()))
                .build(pageForm::addAction);

        return pageForm;
    }

    @Transactional
    @Produces("application/json")
    @GET
    @RolesAllowed({"Administrator", "User", "Guest"})
    public PageForm read(@QueryParam("id") UUID id, @QueryParam("revision") Integer revision) {

        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        if (revision == null) {
            revision = (Integer) auditReader.getRevisionNumberForDate(new Date(Long.MAX_VALUE));
        }

        Page page = auditReader.find(Page.class, id, revision);

        PageForm pageForm = PageForm.factory(page);

        factory.from(PageResource.class)
                .record(pageResource -> pageResource.update(page.getId(), new PageForm()))
                .build(pageForm::addAction);

        factory.from(PageResource.class)
                .record(pageResource -> pageResource.delete(page.getId()))
                .build(pageForm::addAction);

        Property pageLinks = pageForm.getMeta().find("pageLinks");
        factory.from(PagesResource.class)
                .record(pagesResource -> pagesResource.list(new PagesSearch()))
                .build(pageLinks::addLink);

        Property language = pageForm.getMeta().find("language");
        factory.from(ApplicationResource.class)
                .rel("list")
                .record(ApplicationResource::language)
                .build(language::addLink);

        factory.from(QuestionsResource.class)
                .record(questionsResource -> questionsResource.questions(page.getId()))
                .build(pageForm::addLink);

        return pageForm;
    }

    @Transactional
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    @RolesAllowed({"Administrator", "User"})
    public PageForm save(@Valid PageForm resource)  {

        Page page = new Page();

        PageForm.updater(resource, page, identity, entityManager);

        entityManager.persist(page);

        resource.setId(page.getId());

        notificationService.notifyOnPageSave(page);

        factory.from(PageResource.class)
                .record(pageResource -> pageResource.read(page.getId(), 0))
                .build(resource::addAction);

        factory.from(PageResource.class)
                .record(pageResource -> pageResource.update(page.getId(), new PageForm()))
                .build(resource::addAction);

        factory.from(PageResource.class)
                .record(pageResource -> pageResource.delete(page.getId()))
                .build(resource::addAction);

        return resource;
    }

    @Transactional
    @Consumes("application/json")
    @Produces("application/json")
    @PUT
    @RolesAllowed({"Administrator", "User"})
    public PageForm update(@QueryParam("id") UUID id, @Valid PageForm resource) {

        Page page = entityManager.find(Page.class, id);

        PageForm.updater(resource, page, identity, entityManager);

        notificationService.notifyOnPageUpdate(page);

        factory.from(PageResource.class)
                .record(pageResource -> pageResource.read(page.getId(), 0))
                .build(resource::addAction);

        factory.from(PageResource.class)
                .record(pageResource -> pageResource.update(page.getId(), new PageForm()))
                .build(resource::addAction);

        factory.from(PageResource.class)
                .record(pageResource -> pageResource.delete(page.getId()))
                .build(resource::addAction);

        return resource;
    }

    @Transactional
    @DELETE
    @RolesAllowed({"Administrator", "User"})
    public void delete(@QueryParam("id") UUID id) {
        Page page = entityManager.find(Page.class, id);

        List<Question> questions = entityManager.createQuery("select t from Question t where t.page.id = :page", Question.class)
                .setParameter("page", page.getId())
                .getResultList();

        for (Question question : questions) {
            entityManager.remove(question);
        }

        entityManager.remove(page);
    }
}
