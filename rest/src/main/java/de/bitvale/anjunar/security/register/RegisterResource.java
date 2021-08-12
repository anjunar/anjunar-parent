package de.bitvale.anjunar.security.register;

import de.bitvale.anjunar.ApplicationResource;
import de.bitvale.anjunar.control.users.user.UserForm;
import de.bitvale.common.rest.URLBuilderFactory;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Image;
import de.bitvale.common.security.Role;
import de.bitvale.common.security.User;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;

@Path("security/register")
@ApplicationScoped
public class RegisterResource {

    private static final Logger log = LoggerFactory.getLogger(RegisterResource.class);

    private final EntityManager entityManager;

    private final Identity identity;

    private final URLBuilderFactory factory;

    @Inject
    public RegisterResource(EntityManager entityManager, Identity identity, URLBuilderFactory factory) {
        this.entityManager = entityManager;
        this.identity = identity;
        this.factory = factory;
    }

    public RegisterResource() {
        this(null, null, null);
    }

    @GET
    @Produces("application/json")
    public RegisterForm register() {
        RegisterForm resource = new RegisterForm();

        factory.from(RegisterResource.class)
                .record(registerResource -> registerResource.register(new RegisterForm()))
                .build(resource::addAction);

        factory.from(ApplicationResource.class)
                .record(userController -> userController.validate(new UserForm()))
                .build(resource::addSource);

        return resource;
    }

    @POST
    @Consumes("application/json")
    @Transactional
    public void register(@Valid RegisterForm resource) {

        User user = new User();

        user.setFirstName(resource.getFirstName());
        user.setLastName(resource.getLastName());
        user.setBirthDate(resource.getBirthDate());
        user.setPassword(resource.getPassword());
        user.setLanguage(Locale.forLanguageTag("en-DE"));
        user.setEnabled(true);

        try {
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
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }

        entityManager.persist(user);

        Role userRole = entityManager.createQuery("select r from Role r where r.name = :role", Role.class)
                .setParameter("role", "Guest")
                .getSingleResult();

        user.getRoles().add(userRole);

        identity.authenticate(user);

    }

}
