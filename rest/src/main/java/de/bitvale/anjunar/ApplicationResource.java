package de.bitvale.anjunar;

import de.bitvale.anjunar.control.roles.RolesResource;
import de.bitvale.anjunar.control.users.UsersResource;
import de.bitvale.anjunar.control.users.user.UserForm;
import de.bitvale.anjunar.home.timeline.TimelineResource;
import de.bitvale.anjunar.mail.TemplatesResource;
import de.bitvale.anjunar.pages.PagesResource;
import de.bitvale.anjunar.pages.page.PageResource;
import de.bitvale.anjunar.security.login.LoginResource;
import de.bitvale.anjunar.security.logout.LogoutResource;
import de.bitvale.anjunar.security.register.RegisterResource;
import de.bitvale.anjunar.shared.system.Language;
import de.bitvale.anjunar.shared.users.user.UserSelect;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.Container;
import de.bitvale.common.rest.api.Link;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Path("/")
@ApplicationScoped
public class ApplicationResource {

    private final Identity identity;

    private final URLBuilderFactory factory;

    private final EntityManager entityManager;

    @Inject
    public ApplicationResource(Identity identity, URLBuilderFactory factory, EntityManager entityManager) {
        this.identity = identity;
        this.factory = factory;
        this.entityManager = entityManager;
    }

    public ApplicationResource() {
        this(null, null, null);
    }

    @Path("validate")
    @POST
    public Response validate(UserForm resource) {
        User user;
        try {
            user = entityManager.createQuery("select u from User u where u.firstName = :firstName and u.lastName = :lastName and u.birthDate = :birthDate", User.class)
                    .setParameter("firstName", resource.getFirstName())
                    .setParameter("lastName", resource.getLastName())
                    .setParameter("birthDate", resource.getBirthDate())
                    .getSingleResult();
        } catch (NoResultException e) {
            user = null;
        }

        if (resource.getId() == null) {
            if (user == null) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            if (user == null) {
                return Response.ok().build();
            } else {
                if (user.getId().equals(resource.getId())) {
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }
        }
    }

    @GET
    @Transactional
    @Path("lang")
    public Response language(@QueryParam("lang") Locale locale) {

        identity.setLanguage(locale);

        return Response.ok().build();
    }

    @GET
    @Path("language")
    @Produces("application/json")
    public Container<Language> language() {
        List<Language> resources = new ArrayList<>();

        Language german = new Language();
        german.setLanguage("Deutsch");
        german.setLocale(Locale.forLanguageTag("de-DE"));
        resources.add(german);

        Language english = new Language();
        english.setLanguage("English");
        english.setLocale(Locale.forLanguageTag("de-EN"));
        resources.add(english);

        return new Container<>(resources, resources.size());
    }

    @GET
    @Produces("application/json")
    @Transactional
    public UserSelect service() {

        if (identity.isLoggedIn()) {

            UserSelect userSelect = UserSelect.factory(identity.getUser());

            factory.from(LogoutResource.class)
                    .record(LogoutResource::logout)
                    .build(userSelect::addLink);

            factory.from(TimelineResource.class)
                    .record(TimelineResource::timeline)
                    .build(userSelect::addLink);

            factory.from(UsersResource.class)
                    .record(UsersResource::users)
                    .build(userSelect::addLink);

            factory.from(PagesResource.class)
                    .record(pagesSearchController -> pagesSearchController.search(identity.getLanguage()))
                    .build(userSelect::addLink);

            factory.from(PageResource.class)
                    .rel("editor")
                    .record(PageResource::create)
                    .build(userSelect::addLink);

            factory.from(RolesResource.class)
                    .record(RolesResource::roles)
                    .build(userSelect::addLink);

            factory.from(TemplatesResource.class)
                    .record(TemplatesResource::templates)
                    .build(userSelect::addLink);

            if (identity.hasRole("Administrator")) {
                userSelect.addLink(new Link("navigator/navigator", "GET", "navigator"));
            }

            return userSelect;
        } else {
            UserSelect userSelect = new UserSelect();
            userSelect.setLanguage(identity.getLanguage());

            factory.from(LoginResource.class)
                    .record(LoginResource::login)
                    .build(userSelect::addLink);

            factory.from(RegisterResource.class)
                    .record(RegisterResource::register)
                    .build(userSelect::addLink);

            return userSelect;
        }

    }

}
