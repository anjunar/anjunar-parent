package de.bitvale.anjunar.pages;

import de.bitvale.anjunar.pages.page.PageResource;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListResource;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
@Path("pages")
public class PagesResource implements ListResource<PagesForm, PagesSearch> {

    private final Identity identity;

    private final PagesService service;

    private final URLBuilderFactory factory;

    @Inject
    public PagesResource(Identity identity, PagesService service, URLBuilderFactory factory) {
        this.identity = identity;
        this.service = service;
        this.factory = factory;
    }

    public PagesResource() {
        this(null, null, null);
    }

    @GET
    @Produces("application/json")
    public MetaTable search(@QueryParam("lang") Locale lang) {
        MetaTable metaTable = new MetaTable(PagesForm.class);

        metaTable.addSortable(new Sortable[] {
                new Sortable("title", true, true),
                new Sortable("text", false, true)
        });

        PagesSearch search = new PagesSearch();
        search.setLanguage(lang);

        factory.from(PagesResource.class)
                .record(pagesResource -> pagesResource.list(search))
                .build(metaTable::addSource);

        return metaTable;
    }

    @Override
    public Container<PagesForm> list(PagesSearch search) {

        long count = service.count(search);
        List<Page> pages = service.find(search);

        List<PagesForm> resources = new ArrayList<>();
        for (Page page : pages) {
            PagesForm resource = PagesForm.factory(page);
            factory.from(PageResource.class)
                    .record(pageResource -> pageResource.read(page.getId(), null))
                    .build(resource::addAction);

            resources.add(resource);
        }

        Container<PagesForm> container = new Container<>(resources, count);

        factory.from(PageResource.class)
                .record(PageResource::create)
                .build(container::addLink);

        return container;
    }
}
