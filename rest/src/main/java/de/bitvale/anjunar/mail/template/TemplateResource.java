package de.bitvale.anjunar.mail.template;

import de.bitvale.anjunar.ApplicationResource;
import de.bitvale.common.mail.Template;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.FormResource;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.security.Identity;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.UUID;

@Path("mail/templates/template")
@ApplicationScoped
public class TemplateResource implements FormResource<TemplateForm> {

    private final EntityManager entityManager;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public TemplateResource(EntityManager entityManager, Identity identity, URLBuilderFactory factory) {
        this.entityManager = entityManager;
        this.identity = identity;
        this.factory = factory;
    }

    public TemplateResource() {
        this(null, null, null);
    }

    @GET
    @Path("create")
    @Produces("application/json")
    @RolesAllowed("Administrator")
    public TemplateForm create() {
        TemplateForm resource = new TemplateForm();

        resource.setContent(new Editor());

        factory.from(TemplateResource.class)
                .record(templateResource -> templateResource.save(new TemplateForm()))
                .build(resource::addAction);

        Property language = resource.getMeta().find("language");
        factory.from(ApplicationResource.class)
                .rel("list")
                .record(ApplicationResource::language)
                .build(language::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public TemplateForm read(UUID id) {

        Template template = entityManager.find(Template.class, id);

        TemplateForm resource = TemplateForm.factory(template);

        factory.from(TemplateResource.class)
                .record(templateResource -> templateResource.update(template.getId(), new TemplateForm()))
                .build(resource::addAction);

        factory.from(TemplateResource.class)
                .record(templateResource -> templateResource.delete(template.getId()))
                .build(resource::addAction);

        Property language = resource.getMeta().find("language");
        factory.from(ApplicationResource.class)
                .rel("list")
                .record(ApplicationResource::language)
                .build(language::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public TemplateForm save(TemplateForm form) {

        Template template = new Template();

        TemplateForm.updater(form, template, entityManager, identity);

        entityManager.persist(template);

        form.setId(template.getId());

        factory.from(TemplateResource.class)
                .record(templateResource -> templateResource.update(template.getId(), new TemplateForm()))
                .build(form::addAction);

        factory.from(TemplateResource.class)
                .record(templateResource -> templateResource.delete(template.getId()))
                .build(form::addAction);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public TemplateForm update(UUID id, TemplateForm form) {

        Template template = entityManager.find(Template.class, id);

        TemplateForm.updater(form, template, entityManager, identity);

        factory.from(TemplateResource.class)
                .record(templateResource -> templateResource.delete(template.getId()))
                .build(form::addAction);

        return form;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public void delete(UUID id) {
        Template reference = entityManager.getReference(Template.class, id);
        entityManager.remove(reference);
    }
}
