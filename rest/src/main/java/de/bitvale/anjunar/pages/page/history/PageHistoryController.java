package de.bitvale.anjunar.pages.page.history;

import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.shared.users.user.UserResource;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListController;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("pages/page/history")
public class PageHistoryController implements ListController<PageHistoryResource, PageHistorySearch> {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public PageHistoryController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public PageHistoryController() {
        this(null, null);
    }


    @GET
    @Produces("application/json")
    public MetaTable<PageHistoryResource> list(@QueryParam("id") UUID id) {
        MetaTable<PageHistoryResource> metaTable = new MetaTable<>(PageHistoryResource.class, identity.getLanguage());

        metaTable.addSortable(new Sortable[]{
                new Sortable("revision", true, true),
                new Sortable("modified", true, true),
                new Sortable("modifier", true, true)
        });

        identity.createLink("pages/page/history?id=" + id, "POST", "list", metaTable::addSource);

        return metaTable;
    }

    @Override
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<PageHistoryResource> list(PageHistorySearch search) {

        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(Page.class, search.getId());
        List<PageHistoryResource> resources = new ArrayList<>();

        for (Number revision : revisions) {
            Page page = auditReader.find(Page.class, search.getId(), revision);

            PageHistoryResource resource = new PageHistoryResource();

            resource.setId(page.getId());
            resource.setTitle(page.getTitle());
            resource.setContent(page.getContent());
            resource.setText(page.getText());
            resource.setRevision(revision);
            resource.setModified(page.getModified());

            UserResource userResource = new UserResource();

            User modifier = page.getModifier();
            userResource.setId(modifier.getId());
            userResource.setFirstName(modifier.getFirstName());
            userResource.setLastName(modifier.getLastName());
            userResource.setBirthDate(modifier.getBirthDate());

            resource.setModifier(userResource);

            resources.add(resource);
        }

        return new Container<>(resources, resources.size());
    }
}
