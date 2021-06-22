package de.bitvale.anjunar.pages;

import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListController;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("pages/word")
public class PagesWordController implements ListController<PagesResource, PagesWordSearch> {

    private final PagesWordService service;

    private final Identity identity;

    @Inject
    public PagesWordController(PagesWordService service, Identity identity) {
        this.service = service;
        this.identity = identity;
    }

    public PagesWordController() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public MetaTable<PagesResource> list(@QueryParam("title") String title) {
        MetaTable<PagesResource> metaTable = new MetaTable<>(PagesResource.class, identity.getLanguage());

        metaTable.addSortable(new Sortable[]{
                new Sortable("id", false, false),
                new Sortable("title", true, true)
        });

        identity.createLink("pages/word?title=" + title, "POST", "list", metaTable::addSource);

        return metaTable;
    }

    @Override
    @RolesAllowed({"Administrator", "User", "Guest"})
    public Container<PagesResource> list(PagesWordSearch search) {

        List<Page> pages = service.find(search);
        long count = service.count(search);

        List<PagesResource> result = new ArrayList<>();
        for (Page page : pages) {
            PagesResource resource = PagesResource.factory(page);

            result.add(resource);

            identity.createLink("pages/page?id=" + page.getId(), "GET", "read", resource::addAction);


        }

        Container<PagesResource> container = new Container<>(result, count);

        identity.createLink("pages/page/create", "GET", "create", container::addLink);

        return container;
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    @RolesAllowed({"Administrator", "User", "Guest"})
    public PagesResource word(PagesResource resource) {

        identity.createLink("pages/word?title=" + resource.getTitle(), "GET", "redirect", resource::addLink);

        return resource;
    }


}
