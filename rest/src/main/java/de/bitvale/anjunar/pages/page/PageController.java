package de.bitvale.anjunar.pages.page;

import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.pages.page.forum.Question;
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
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page")
public class PageController {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public PageController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public PageController() {
        this(null, null);
    }

    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed({"Administrator", "User"})
    public PageForm create() {

        PageForm pageForm = new PageForm();

        identity.createLink("pages/page", "POST", "save", pageForm::addAction);
        identity.createLink("pages/page/questions", "GET", "questions", pageForm::addLink);

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

        identity.createLink("pages/page?id=" + page.getId(), "PUT", "update", pageForm::addAction);
        identity.createLink("pages/page?id=" + page.getId(), "DELETE", "delete", pageForm::addAction);

        identity.createLink("pages/page/images", "GET", "images", pageForm::addSource);
        identity.createLink("pages/page/images/image", "GET", "upload", pageForm::addSource);
        identity.createLink("pages/page/questions?page=" + page.getId(), "GET", "questions", pageForm::addLink);

        return pageForm;
    }

    @Transactional
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    @RolesAllowed({"Administrator", "User"})
    public PageForm save(@Valid PageForm resource) throws UnsupportedEncodingException {

        Page page = new Page();

        PageForm.updater(resource, page, identity, entityManager);

        entityManager.persist(page);

        resource.setId(page.getId());

        identity.createLink("pages/page?id=" + page.getId(), "GET", "read", resource::addAction);
        identity.createLink("pages/page?id=" + page.getId(), "PUT", "update", resource::addAction);
        identity.createLink("pages/page?id=" + page.getId(), "DELETE", "delete", resource::addAction);

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

        identity.createLink("pages/page?id=" + page.getId(), "GET", "read", resource::addAction);
        identity.createLink("pages/page?id=" + page.getId(), "PUT", "update", resource::addAction);
        identity.createLink("pages/page?id=" + page.getId(), "DELETE", "delete", resource::addAction);

        return resource;
    }

    @Transactional
    @DELETE
    @RolesAllowed({"Administrator", "User"})
    public void delete(@QueryParam("id") UUID id) {
        Page page = entityManager.find(Page.class, id);

        List<Question> questions = entityManager.createQuery("select t from Question t where t.page = :page", Question.class)
                .setParameter("page", page.getId())
                .getResultList();

        for (Question question : questions) {
            entityManager.remove(question);
        }

        entityManager.remove(page);
    }
}
