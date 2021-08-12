package de.bitvale.anjunar.pages.page.history;

import de.bitvale.anjunar.pages.Page;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListResource;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;
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
public class PageHistoryResource implements ListResource<PageHistoryForm, PageHistorySearch> {

    private final EntityManager entityManager;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public PageHistoryResource(EntityManager entityManager, Identity identity, URLBuilderFactory factory) {
        this.entityManager = entityManager;
        this.identity = identity;
        this.factory = factory;
    }

    public PageHistoryResource() {
        this(null, null, null);
    }


    @GET
    @Produces("application/json")
    public MetaTable list(@QueryParam("id") UUID id) {
        MetaTable metaTable = new MetaTable(PageHistoryForm.class);

        metaTable.addSortable(new Sortable[]{
                new Sortable("revision", true, true),
                new Sortable("modified", true, true),
                new Sortable("modifier", true, true)
        });

        factory.from(PageHistoryResource.class)
                .record(pageHistoryController -> pageHistoryController.list(new PageHistorySearch()))
                .build(metaTable::addSource);

        return metaTable;
    }

    @Override
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<PageHistoryForm> list(PageHistorySearch search) {

        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Number> revisions = auditReader.getRevisions(Page.class, search.getId());
        List<PageHistoryForm> resources = new ArrayList<>();

        for (Number revision : revisions) {
            Page page = auditReader.find(Page.class, search.getId(), revision);

            PageHistoryForm resource = PageHistoryForm.factory(page, revision);

            resources.add(resource);
        }

        return new Container<>(resources, resources.size());
    }
}
