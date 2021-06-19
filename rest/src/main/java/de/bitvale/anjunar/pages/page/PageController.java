package de.bitvale.anjunar.pages.page;

import de.bitvale.anjunar.pages.page.forum.Topic;
import de.bitvale.common.rest.Secured;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.MetaForm;
import de.bitvale.common.security.Identity;
import de.bitvale.anjunar.pages.Page;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page")
@Secured
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
    public MetaForm<PageForm> create() {

        PageForm pageForm = new PageForm();
        pageForm.setContent(new Editor());
        identity.createLink("pages/page", "POST", "save", (pageForm::addAction));
        identity.createLink("pages/page/topics", "GET", "topics", pageForm::addLink);

        return new MetaForm<>(pageForm, identity.getLanguage());
    }

    @Transactional
    @Produces("application/json")
    @GET
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaForm<PageForm> read(@QueryParam("id") UUID id, @QueryParam("revision") Integer revision) {

        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        if (revision == null) {
            revision = (Integer) auditReader.getRevisionNumberForDate(new Date(Long.MAX_VALUE));
        }

        Page page = auditReader.find(Page.class, id, revision);

        PageForm pageForm = new PageForm();

        pageForm.setId(page.getId());
        Editor editor = new Editor();
        editor.setHtml(page.getContent());
        editor.setText(page.getText());
        pageForm.setContent(editor);
        pageForm.setTitle(page.getTitle());
        pageForm.setLanguage(page.getLanguage());

        for (Page pageLink : page.getLinks()) {
            PageSelect pageSelect = new PageSelect();
            pageSelect.setId(pageLink.getId());
            pageSelect.setTitle(pageLink.getTitle());
            pageSelect.setLanguage(pageLink.getLanguage());
            pageForm.getPageLinks().add(pageSelect);
        }

        identity.createLink("pages/page?id=" + page.getId(), "PUT", "update", pageForm::addAction);
        identity.createLink("pages/page?id=" + page.getId(), "DELETE", "delete", pageForm::addAction);

        MetaForm<PageForm> metaForm = new MetaForm<>(pageForm, identity.getLanguage());

        identity.createLink("pages/page/images", "GET", "images", metaForm::addSource);
        identity.createLink("pages/page/images/image", "GET", "upload", metaForm::addSource);
        identity.createLink("pages/page/topics?page=" + page.getId(), "GET", "topics", metaForm::addLink);

        return metaForm;
    }

    @Transactional
    @Consumes("application/json")
    @Produces("application/json")
    @POST
    @RolesAllowed({"Administrator", "User"})
    public PageForm save(PageForm resource) throws UnsupportedEncodingException {

        Page page = new Page();
        page.setTitle(resource.getTitle());
        page.setContent(resource.getContent().getHtml());
        page.setText(resource.getContent().getText());
        page.setModifier(identity.getUser());
        page.setLanguage(resource.getLanguage());
        entityManager.persist(page);
        resource.setId(page.getId());

        page.getLinks().clear();
        for (PageSelect pageSelect : resource.getPageLinks()) {
            Page pageLink = entityManager.find(Page.class, pageSelect.getId());
            page.getLinks().add(pageLink);
        }

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
    public PageForm update(@QueryParam("id") UUID id, PageForm resource) {

        Page page = entityManager.find(Page.class, id);
        page.setTitle(resource.getTitle());
        page.setContent(resource.getContent().getHtml());
        page.setText(resource.getContent().getText());
        page.setModifier(identity.getUser());
        page.setLanguage(resource.getLanguage());

        page.getLinks().clear();
        for (PageSelect pageSelect : resource.getPageLinks()) {
            Page pageLink = entityManager.find(Page.class, pageSelect.getId());
            page.getLinks().add(pageLink);
        }

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

        List<Topic> topics = entityManager.createQuery("select t from Topic t where t.page = :page", Topic.class)
                .setParameter("page", page.getId())
                .getResultList();

        for (Topic topic : topics) {
            entityManager.remove(topic);
        }

        entityManager.remove(page);
    }
}
