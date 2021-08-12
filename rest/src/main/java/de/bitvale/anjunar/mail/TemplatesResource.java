package de.bitvale.anjunar.mail;

import de.bitvale.anjunar.mail.template.TemplateResource;
import de.bitvale.anjunar.mail.template.TemplateForm;
import de.bitvale.common.mail.Template;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.ListResource;
import de.bitvale.common.rest.api.meta.MetaTable;
import de.bitvale.common.rest.api.meta.Sortable;
import de.bitvale.common.security.Identity;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Path("mail/templates")
@ApplicationScoped
public class TemplatesResource implements ListResource<TemplateForm, TemplatesSearch> {

    private final Identity identity;

    private final TemplatesService service;

    private final URLBuilderFactory factory;

    @Inject
    public TemplatesResource(Identity identity, TemplatesService service, URLBuilderFactory factory) {
        this.identity = identity;
        this.service = service;
        this.factory = factory;
    }

    public TemplatesResource() {
        this(null, null, null);
    }

    @GET
    @Produces("application/json")
    @RolesAllowed("Administrator")
    public MetaTable templates() {
        MetaTable metaTable = new MetaTable(TemplateForm.class);

        metaTable.addSortable(new Sortable[]{
                new Sortable("name", true, true),
                new Sortable("content", false, true)
        });

        factory.from(TemplatesResource.class)
                .record(templatesResource -> templatesResource.list(new TemplatesSearch()))
                .build(metaTable::addSource);

        return metaTable;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public Container<TemplateForm> list(TemplatesSearch search) {

        List<Template> templates = service.find(search);
        long count = service.count(search);

        List<TemplateForm> resources = new ArrayList<>();

        for (Template template : templates) {

            TemplateForm resource = TemplateForm.factory(template);

            factory.from(TemplateResource.class)
                    .record(templateResource -> templateResource.read(template.getId()))
                    .build(resource::addAction);

            resources.add(resource);

        }

        Container<TemplateForm> container = new Container<>(resources, count);

        factory.from(TemplateResource.class)
                .record(TemplateResource::create)
                .build(container::addLink);

        return container;
    }
}
