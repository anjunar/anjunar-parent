package de.bitvale.anjunar.pages;

import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListMetaController;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("pages")
public class PagesController implements ListMetaController<PagesResource, PagesSearch> {

    private final Identity identity;

    private final PagesService service;

    @Inject
    public PagesController(Identity identity, PagesService service) {
        this.identity = identity;
        this.service = service;
    }

    public PagesController() {
        this(null, null);
    }

    @Override
    public MetaTable<PagesResource> list() {
        MetaTable<PagesResource> metaTable = new MetaTable<>(PagesResource.class, identity.getLanguage());

        metaTable.addSortable(new Sortable[] {
                new Sortable("title", true, true),
                new Sortable("text", false, true)
        });

        identity.createLink("pages", "GET", "list", metaTable::addSource);

        return metaTable;
    }

    @Override
    public Container<PagesResource> list(PagesSearch search) {

        long count = service.count(search);
        List<Page> pages = service.find(search);

        List<PagesResource> resources = new ArrayList<>();
        for (Page page : pages) {
            PagesResource resource = PagesResource.factory(page);

            identity.createLink("pages/page?id=" + page.getId(), "GET", "read", resource::addAction);

            resources.add(resource);
        }

        Container<PagesResource> container = new Container<>(resources, count);

        identity.createLink("pages/page/create", "GET", "create", container::addLink);

        return container;
    }
}
