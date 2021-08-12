package de.bitvale.anjunar.control.users.user;

import com.google.common.base.Strings;
import de.bitvale.anjunar.ApplicationResource;
import de.bitvale.anjunar.control.roles.RolesResource;
import de.bitvale.anjunar.control.roles.RolesSearch;
import de.bitvale.anjunar.security.login.LoginResource;
import de.bitvale.anjunar.security.register.RegisterResource;
import de.bitvale.common.mail.Email;
import de.bitvale.common.mail.Template;
import de.bitvale.common.rest.MethodPredicate;
import de.bitvale.common.rest.SelfIdentity;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.rest.api.FormResource;
import de.bitvale.common.rest.api.meta.Property;
import de.bitvale.common.rest.api.meta.hints.PropertyHint;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Image;
import de.bitvale.common.security.User;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

@RequestScoped
@Path("control/users/user")
public class UserResource implements FormResource<UserForm> {

    private static final Logger log = LoggerFactory.getLogger(RegisterResource.class);

    private final EntityManager entityManager;

    private final Identity identity;

    private final URLBuilderFactory factory;

    private final Email email;

    private final HttpServletRequest request;

    @Inject
    public UserResource(EntityManager entityManager, Identity identity, URLBuilderFactory factory, Email email, @Context HttpServletRequest request) {
        this.entityManager = entityManager;
        this.identity = identity;
        this.factory = factory;
        this.email = email;
        this.request = request;
    }

    public UserResource() {
        this(null, null, null, null, null);
    }

    @Path("confirm")
    @GET
    @RolesAllowed({"Administrator", "User"})
    @Produces("application/json")
    @Transactional
    public Response confirm(@QueryParam("id") UUID id, @QueryParam("hash") String hash) {
        User user = entityManager.find(User.class, id);

        if (user.getEmailConfirmationHash().equals(hash)) {
            user.setEmailConfirmed(true);
            return Response.ok("true").build();
        }

        user.setEmailConfirmed(false);
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @Transactional
    @Produces("application/json")
    @GET
    @Path("create")
    @RolesAllowed("Administrator")
    public UserForm create() {
        UserForm resource = new UserForm();

        factory.from(ApplicationResource.class)
                .record(userController -> userController.validate(new UserForm()))
                .build(resource::addSource);

        factory.from(UserResource.class)
                .record(userResource -> userResource.save(new UserForm()))
                .build(resource::addAction);

        Property roles = resource.getMeta().find("roles");
        factory.from(RolesResource.class)
                .record(roleController -> roleController.list(new RolesSearch()))
                .build(roles::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    public UserForm read(UUID id) {

        User user = entityManager.find(User.class, id);

        UserForm resource = UserForm.factory(user);

        if (! Strings.isNullOrEmpty(user.getEmail())) {
            ResourceBundle placeholderBundle = ResourceBundle.getBundle("de.bitvale.anjunar.i18nMessages", identity.getLanguage());
            Property email = resource.getMeta().find("email");
            if (user.isEmailConfirmed()) {
                email.addHint(new PropertyHint("confirmed", placeholderBundle.getString("de.bitvale.anjunar.control.users.user.UserResource.confirmed")));
            } else {
                email.addHint(new PropertyHint("confirmed", placeholderBundle.getString("de.bitvale.anjunar.control.users.user.UserResource.notConfirmed")));
            }
        }

        if (identity.hasRole("Administrator") || identity.getUser().equals(user)) {
            resource.setPassword(user.getPassword());
        }

        factory.from(UserResource.class)
                .record(userResource -> userResource.update(id, new UserForm()))
                .build(resource::addAction);

        factory.from(UserResource.class)
                .record(userResource -> userResource.delete(id))
                .build(resource::addAction);

        factory.from(LoginResource.class)
                .record(loginResource -> loginResource.runAs(id))
                .build(resource::addAction);

        factory.from(ApplicationResource.class)
                .record(userController -> userController.validate(new UserForm()))
                .build(resource::addSource);

        Property roles = resource.getMeta().find("roles");
        factory.from(RolesResource.class)
                .record(roleController -> roleController.list(new RolesSearch()))
                .build(roles::addLink);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed("Administrator")
    public UserForm save(@Valid UserForm resource) {

        User user = new User();

        UserForm.updater(resource, user, entityManager, identity);

        user.setPassword(resource.getPassword());

        if (! Strings.isNullOrEmpty(resource.getEmail())) {
            confirmationEmail(user);
        }

        try {
            if (user.getPicture() == null) {
                URL picture = getClass()
                        .getClassLoader()
                        .getResource("META-INF/resources/user.png");
                InputStream inputStream = picture.openStream();
                byte[] bytes = new byte[inputStream.available()];
                IOUtils.readFully(inputStream, bytes);
                Image image = new Image();
                image.setName("user.png");
                image.setData(bytes);
                image.setLastModified(LocalDateTime.now());
                image.setType("image");
                image.setSubType("png");
                user.setPicture(image);
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }


        entityManager.persist(user);

        resource.setId(user.getId());

        factory.from(UserResource.class)
                .record(userResource -> userResource.update(user.getId(), new UserForm()))
                .build(resource::addAction);

        factory.from(UserResource.class)
                .record(userResource -> userResource.delete(user.getId()))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User", "Guest"})
    @MethodPredicate(SelfIdentity.class)
    public UserForm update(UUID id, @Valid UserForm resource) {
        User user = entityManager.find(User.class, id);

        UserForm.updater(resource, user, entityManager, identity);

        if (identity.hasRole("Administrator") || identity.getUser().equals(user)) {
            user.setPassword(resource.getPassword());
        }

        if (! Strings.isNullOrEmpty(resource.getEmail())) {
            confirmationEmail(user);
        }

        factory.from(UserResource.class)
                .record(userResource -> userResource.update(id, new UserForm()))
                .build(resource::addAction);

        factory.from(UserResource.class)
                .record(userResource -> userResource.delete(id))
                .build(resource::addAction);

        return resource;
    }

    @Override
    @Transactional
    @RolesAllowed({"Administrator", "User"})
    @MethodPredicate(SelfIdentity.class)
    public void delete(UUID id) {
        User user = entityManager.find(User.class, id);

        entityManager.remove(user);
    }

    private void confirmationEmail(User user) {
        if (!Strings.isNullOrEmpty(user.getEmail()) && !user.isEmailConfirmed()) {
            Template template = null;
            try {
                template = entityManager.createQuery("select t from Template t where t.name = :name and t.language = :language", Template.class)
                        .setParameter("name", "Email Confirmation")
                        .setParameter("language", identity.getLanguage())
                        .getSingleResult();

                Random rnd = new Random();
                int number = rnd.nextInt(9999);
                String hash = String.format("%4d", number).replace(" ", "0");
                user.setEmailConfirmationHash(hash);

                String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

                String replaced = template.getHtml();
                replaced = replaced.replace("{{firstName}}", user.getFirstName());
                replaced = replaced.replace("{{lastName}}", user.getLastName());
                replaced = replaced.replace("{{link}}", url + "#/anjunar/control/confirm?hash=" + hash + "&id=" + user.getId());

                email.send(user.getEmail(), "Email Confirmation", replaced);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
        }
    }

}
