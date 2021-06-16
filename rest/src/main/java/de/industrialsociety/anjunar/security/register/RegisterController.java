package de.industrialsociety.anjunar.security.register;

import de.industrialsociety.common.rest.api.Link;
import de.industrialsociety.common.rest.api.meta.MetaForm;
import de.industrialsociety.common.security.Identity;
import de.industrialsociety.common.security.Role;
import de.industrialsociety.common.security.Relationship;
import de.industrialsociety.common.security.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;

@Path("security/register")
@ApplicationScoped
public class RegisterController {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public RegisterController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public RegisterController() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    public MetaForm<RegisterResource> register() {
        RegisterResource resource = new RegisterResource();

        resource.addAction(new Link("security/register", "POST", "register"));

        return new MetaForm<>(resource, identity.getLanguage());
    }

    @POST
    @Consumes("application/json")
    @Transactional
    public void register(RegisterResource resource) {

        User user = new User();

        user.setFirstName(resource.getFirstName());
        user.setLastName(resource.getLastName());
        user.setBirthDate(resource.getBirthdate());
        user.setPassword(resource.getPassword());
        user.setEnabled(true);

        entityManager.persist(user);

        Role userRole = entityManager.createQuery("select r from Role r where r.name = :role", Role.class)
                .setParameter("role", "User")
                .getSingleResult();

        Relationship relationship = new Relationship();
        relationship.setGroup(userRole);
        relationship.setUser(user);

        entityManager.persist(relationship);

        identity.authenticate(user);


    }

}
